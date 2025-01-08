import PlayerSearchForm from "@/components/player-search-form";

export default async function PlayerNotFound() {

  return (
    <div className="space-y-10 my-10">
      <div className="text-center space-y-2">
        <h1 className="text-2xl font-bold">플레이어를 찾을 수 없습니다.</h1>
        <p className="text-gray-500">다른 플레이어를 검색해보세요.</p>
      </div>

      <div className="flex justify-center">
        <PlayerSearchForm/>
      </div>
    </div>
  );
};