"use client";

import {LoadingButton} from "@/components/ui/expansion/loading-button";
import {useState, useEffect, useCallback} from "react";
import {Player} from "@/model/Player";
import {getPlayerRenewalStatus, renewPlayer} from "@/lib/api/player";
import {ApiErrorTypeValue} from "@/model/response/ApiResponse";
import {toast} from "sonner";
import {useInterval} from "usehooks-ts";

export default function PlayerRenewButton({player}: Readonly<{ player: Player }>) {
  const [renewalEnabled, setRenewalEnabled] = useState(false);
  const [loading, setLoading] = useState(false);
  const [remainingSeconds, setRemainingSeconds] = useState(0);

  const calculateTimeStatus = useCallback(() => {
    const current = new Date();
    const playerUpdatedAt = player.updatedAt;
    console.log(`current: ${current}`);
    console.log(`playerUpdatedAt: ${playerUpdatedAt}`);
    const timeDifferenceInSeconds = Math.floor((current.getTime() - playerUpdatedAt.getTime()) / 1000);
    const requiredWaitTime = 2 * 60;
    const remaining = Math.max(0, requiredWaitTime - timeDifferenceInSeconds);

    setRemainingSeconds(remaining);
    setRenewalEnabled(remaining === 0);
  }, [player.updatedAt]);

  useInterval(calculateTimeStatus, !renewalEnabled ? 1000 : null);

  useEffect(() => {
    calculateTimeStatus();
  }, [player.updatedAt, calculateTimeStatus]);

  const handleRenew = async () => {

    setLoading(true);
    try {
      const response = await renewPlayer(player.tag);
      if (response.ok) {
        setRenewalEnabled(false);
        
        const checkRenewalStatus = async () => {
          const status = await getPlayerRenewalStatus(player.tag);
          if (!status.renewing) {
            window.location.reload();
          } else {
            setTimeout(checkRenewalStatus, 1000);
          }
        };

        await checkRenewalStatus();

      } else if (response.error?.type === ApiErrorTypeValue.PLAYER_ALREADY_RENEWED) {
        toast("아직 새로고침이 불가능합니다. 잠시 후 다시 시도해주세요.");
      } else if (response.error?.type === ApiErrorTypeValue.PLAYER_RENEW_UNAVAILABLE) {
        toast("현재 새로고침 요청이 많아서 처리할 수 없습니다. 잠시 후 다시 시도해주세요.");
      }
    } catch (error) {
      console.error('Failed to renew player:', error);
    } finally {
      setLoading(false);
    }
  }

  return (
    <LoadingButton
      loading={loading}
      disabled={!renewalEnabled}
      onClick={handleRenew}
      size="sm" value="outline"
      className="bg-blue-400 rounded-sm"
    >
      {renewalEnabled ? '새로고침' : `${remainingSeconds}초 후 시도`}
    </LoadingButton>
  );
}