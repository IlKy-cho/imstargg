export default function PlayerSearch() {
    return (
        <div className="w-full max-w-xl">
        <div className="relative">
          <input
            type="text"
            placeholder="플레이어 명 혹은 태그를 입력하세요"
            className="w-full px-4 py-3 rounded-full text-white border border-gray-600 focus:outline-none focus:border-blue-500"
          />
          <button className="absolute right-3 top-1/2 transform -translate-y-1/2 bg-blue-500 text-white px-4 py-1 rounded-full hover:bg-blue-600">
            검색
          </button>
        </div>
      </div>
    );
}