import {Player} from "@/model/Player";
import {useRouter} from "next/navigation";
import dayjs from "dayjs";
import 'dayjs/locale/ko';
import relativeTime from 'dayjs/plugin/relativeTime';
import SoloRankTier from "@/components/solo-rank-tier";
import Trophy from "@/components/trophy";

dayjs.locale('ko');
dayjs.extend(relativeTime);


interface SearchedPlayerProps {
  player: Player;
}

export default function SearchedPlayer({player}: SearchedPlayerProps) {
  const router = useRouter();

  return (
    <div
      className="p-4 border rounded-lg cursor-pointer hover:bg-zinc-100"
      onClick={() => router.push(`/players/${encodeURIComponent(player.tag)}`)}
    >
      <div className="flex items-center gap-2">
        <span style={{color: player.nameColor}}>{player.name}</span>
        <span className="text-gray-500">{player.tag}</span>
      </div>
      <div className="text-sm text-gray-600 flex items-center justify-between">
        <div className="flex items-center gap-1">
          <Trophy value={player.trophies}/>
          {player.soloRankTier ?
            <SoloRankTier tier={player.soloRankTier}/>
            : <span className="text-gray-400">❓</span>
          }
        </div>
        <div>
          최근 업데이트: {dayjs(player.updatedAt).fromNow()}
        </div>
      </div>
    </div>
  );
}