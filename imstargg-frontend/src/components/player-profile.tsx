import {Player} from "@/model/Player";
import 'dayjs/locale/ko';
import dayjs from "dayjs";
import relativeTime from "dayjs/plugin/relativeTime";

dayjs.locale('ko');
dayjs.extend(relativeTime);

type Props = {
  player: Player;
}

export default async function PlayerProfile({player}: Readonly<Props>) {

  return (
    <div className="p-6 bg-white rounded-lg shadow-sm border border-gray-100">
      <div className="flex items-start gap-6">
        <div className="w-24 h-24 relative flex items-center justify-center bg-gray-50 rounded-lg border border-gray-200">
          <span className="text-gray-600 text-sm">아이콘 ID: {player.iconId}</span>
        </div>

        <div className="flex flex-col gap-3">
          <div>
            <h2 className="text-2xl font-bold" style={{color: player.nameColor}}>
              {player.name}
            </h2>
            <p className="text-gray-500">#{player.tag}</p>
          </div>

          <div className="grid grid-cols-2 gap-6">
            <div>
              <p className="text-gray-500 text-sm">트로피</p>
              <p className="text-lg font-semibold text-amber-500">
                �� {player.trophies.toLocaleString()}
              </p>
            </div>
            <div>
              <p className="text-gray-500 text-sm">최고 트로피</p>
              <p className="text-lg font-semibold text-amber-500">
                🏆 {player.highestTrophies.toLocaleString()}
              </p>
            </div>
          </div>

          {player.clubTag && (
            <div>
              <p className="text-gray-500 text-sm">클럽</p>
              <p className="text-blue-500 font-medium">#{player.clubTag}</p>
            </div>
          )}

          <div className="text-sm text-gray-400">
            마지막 업데이트: {dayjs(player.updatedAt).fromNow()}
          </div>
        </div>
      </div>
    </div>
  );
}