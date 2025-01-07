'use client';

import {useEffect} from 'react'
import Image from 'next/image';
import {Button} from "@/components/ui/button";
import edgarSadPinSrc from '@/../public/icon/brawler/edgar/edgar_sad_pin.png';

export default function Error({error, reset,}: {
  error: Error & { digest?: string }
  reset: () => void
}) {

  useEffect(() => {
    console.error(error)
  }, [error]);

  return (
    <div className="flex flex-col items-center justify-center min-h-screen text-center p-4">
      <h1 className="text-2xl font-bold mb-4">서버에 문제가 발생했습니다...</h1>

      <div className="mb-2">
        <p>서버에 알 수 없는 문제가 발생했습니다.</p>
        <p>잠시 후에 다시 시도해주세요.</p>
      </div>

      <div className="mb-2">
        <p>관련 문의사항이 있다면 남겨주세요.</p>
      </div>

      <div className="mb-8">
        <Image
          src={edgarSadPinSrc}
          alt="에러 이미지"
        />
      </div>

      <Button onClick={reset}>
        다시시도
      </Button>
    </div>
  )
}