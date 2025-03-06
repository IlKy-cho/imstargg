'use client';

import {useEffect} from 'react'
import Image from 'next/image';
import {Button} from "@/components/ui/button";
import fangSadPinSrc from '@/../public/icon/brawler/fang/fang_sad_pin.png';
import {ApiError, ApiErrorTypeValue} from "@/lib/api/api";

export default function PlayerError({error, reset,}: {
  error: Error & { digest?: string }
  reset: () => void
}) {

  useEffect(() => {
    console.error('Player error:', error);
  }, [error]);

  if (!(error instanceof ApiError)) {
    throw error;
  }

  const apiError = error as ApiError;
  if (!apiError.error) {
    throw error;
  }

  if (apiError.error.type !== ApiErrorTypeValue.PLAYER_RENEW_UNAVAILABLE
    && apiError.error.type !== ApiErrorTypeValue.PLAYER_RENEWAL_TOO_MANY
    && apiError.error.type !== ApiErrorTypeValue.BRAWLSTARS_IN_MAINTENANCE) {
    throw error;
  }

  return (
    <div className="flex flex-col items-center justify-center min-h-screen text-center p-4">
      <h1 className="text-2xl font-bold mb-4">플레이어 갱신 중 문제가 발생했습니다.</h1>

      <div className="mb-2">
        <p>{apiError.error.message}</p>
      </div>

      <div className="mb-2">
        <p>관련 문의사항이 있다면 남겨주세요.</p>
      </div>

      <div className="mb-8">
        <Image
          src={fangSadPinSrc}
          alt="에러 이미지"
        />
      </div>

      <Button onClick={reset}>
        다시시도
      </Button>
    </div>
  )
}