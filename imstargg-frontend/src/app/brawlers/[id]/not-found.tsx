import Image from 'next/image';
import leonPhewPinSrc from '@/../public/icon/brawler/leon/leon_phew_pin.png';
import {Button} from "@/components/ui/button";
import Link from "next/link";

export default async function BrawlerNotFound() {

  return (
    <div className="flex flex-col items-center justify-center min-h-screen text-center p-4">
      <h1 className="text-2xl font-bold mb-4">존재하지 않는 존재하지 않는 브롤러입니다 !</h1>

      <div className="mb-2">
        <p>해당 브롤러는</p>
        <p>존재하지 않거나 아직 준비중입니다.</p>
      </div>

      <div className="mb-8">
        <Image
          src={leonPhewPinSrc}
          alt="brawler-not-found-image"
        />
      </div>

      <div className="flex gap-2">
        <Button asChild>
          <Link href="/">
            홈
          </Link>
        </Button>
        <Button asChild>
          <Link href="/brawlers">
            브롤러 목록
          </Link>
        </Button>
      </div>
    </div>
  );
}