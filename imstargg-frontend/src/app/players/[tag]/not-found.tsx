import PlayerSearchForm from "@/components/player-search-form";
import beaPhewPinSrc from '@/../public/icon/brawler/bea/bea_phew_pin.png';
import Image from 'next/image';

export default async function PlayerNotFound() {

  return (
    <div className="flex flex-col items-center justify-center min-h-screen text-center p-4 gap-10">
      <div className="text-center space-y-2">
        <h1 className="text-2xl font-bold">플레이어를 찾을 수 없습니다.</h1>
        <p className="text-gray-500">다른 플레이어를 검색해보세요.</p>
      </div>

      <PlayerSearchForm/>

      <div className="mb-8">
        <Image
          src={beaPhewPinSrc}
          alt="player-not-found-image"
        />
      </div>
    </div>
  );
};