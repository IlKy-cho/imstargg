"use client";

import {LoadingButton} from "@/components/ui/expansion/loading-button";
import {useCallback, useEffect, useState} from "react";
import {Player} from "@/model/Player";
import {getPlayerRenewalStatus, renewPlayer} from "@/lib/api/player";
import {toast} from "sonner";
import {useInterval} from "usehooks-ts";
import {ApiError, ApiErrorTypeValue} from "@/lib/api/api";
import {processPlayerRenewal} from "@/lib/player";

export default function PlayerRenewButton({player}: Readonly<{ player: Player }>) {
  const [renewalEnabled, setRenewalEnabled] = useState(false);
  const [loading, setLoading] = useState(false);
  const [remainingSeconds, setRemainingSeconds] = useState(0);

  const calculateTimeStatus = useCallback(() => {
    const current = new Date();
    const playerUpdatedAt = player.updatedAt;
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
    setRenewalEnabled(false);
    try {
      await processPlayerRenewal(player.tag);
      window.location.reload();
    } catch (error) {
      if (error instanceof ApiError) {
        if (error.error?.type === ApiErrorTypeValue.PLAYER_RENEW_UNAVAILABLE) {
          toast("현재 새로고침 요청이 많아서 처리할 수 없습니다. 잠시 후 다시 시도해주세요.");
        } else if (error.error?.type === ApiErrorTypeValue.BRAWLSTARS_IN_MAINTENANCE) {
          toast("브롤스타즈 서버 점검 중입니다. 잠시 후 다시 시도해주세요.");
        }
      } else {
        console.error('Unexpected error:', error);
        toast("예기치 않은 오류가 발생했습니다.");
      }
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
      {remainingSeconds === 0 ? '새로고침' : `${remainingSeconds}초 후 시도`}
    </LoadingButton>
  );
}