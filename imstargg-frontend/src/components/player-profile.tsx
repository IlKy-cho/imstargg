import {Player} from "@/model/Player";
import Image from "next/image";
import {BrawlStarsIconSrc} from "@/components/icon";
import dayjs from "dayjs";
import 'dayjs/locale/ko';
import relativeTime from "dayjs/plugin/relativeTime";
import SoloRankTier from "@/components/solo-rank-tier";
import Trophy from "@/components/trophy";

dayjs.locale('ko');
dayjs.extend(relativeTime);

type Props = {
  player: Player;
}

const PlayerInfoContainer = ({label, children}: { label: string, children: React.ReactNode }) => (
  <div className="flex justify-between items-center">
    <span className="text-zinc-500">{label}</span>
    {children}
  </div>
);

const PlayerInfo = ({player}: Readonly<Props>) => (
  <div className="p-6 rounded-lg shadow-md border bg-zinc-100 bg-opacity-90 m-2 max-w-screen-sm">
    <div className="space-y-">
      <PlayerInfoContainer label="이름">
        <span>{player.name}</span>
      </PlayerInfoContainer>

      <PlayerInfoContainer label="태그">
        <span>{player.tag}</span>
      </PlayerInfoContainer>

      <PlayerInfoContainer label="클럽 태그">
        {player.clubTag ?
          <span>{player.clubTag}</span>
          : <span className="text-gray-400">❌</span>
        }
      </PlayerInfoContainer>

      <PlayerInfoContainer label="트로피">
        <Trophy value={player.trophies}/>
      </PlayerInfoContainer>

      <PlayerInfoContainer label="최고 트로피">
        <Trophy value={player.highestTrophies}/>
      </PlayerInfoContainer>

      <PlayerInfoContainer label="경쟁전">
        {player.soloRankTier ?
          <SoloRankTier tier={player.soloRankTier}/>
          : <span className="text-gray-400">❓</span>
        }
      </PlayerInfoContainer>

      <div className="text-sm text-gray-400">
        마지막 업데이트: {dayjs(player.updatedAt).fromNow()}
      </div>
    </div>
  </div>
);

export default function PlayerProfile({player}: Readonly<Props>) {
  return (
    <PlayerInfo player={player}/>
  );
}