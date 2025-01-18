import Image from 'next/image';
import Link from 'next/link';
import {Button} from "@/components/ui/button";
import edgarPhewPinSrc from '@/../public/icon/brawler/edgar/edgar_phew_pin.png';

export default async function NotFound() {
  return (
    <div className="flex flex-col items-center justify-center min-h-screen text-center p-4">
      <h1 className="text-2xl font-bold mb-4">존재하지 않는 페이지입니다!</h1>

      <div className="mb-2">
        <p>지금 입력하신 주소의 페이지는</p>
        <p>사라졌거나 다른 페이지로 변경되었습니다.</p>
        <p>주소를 다시 확인해주세요.</p>
      </div>

      <div className="mb-2">
        <p>관련 문의사항이 있다면 남겨주세요.</p>
      </div>

      <div className="mb-8">
        <Image
          src={edgarPhewPinSrc}
          alt="404 이미지"
        />
      </div>

      <Button asChild>
        <Link href="/">
          홈
        </Link>
      </Button>
    </div>
  );
}
