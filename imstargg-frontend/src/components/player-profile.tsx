import {Player} from "@/model/Player";
import dayjs from "dayjs";
import 'dayjs/locale/ko';
import relativeTime from "dayjs/plugin/relativeTime";
import SoloRankTier from "@/components/solo-rank-tier";
import Trophy from "@/components/trophy";
import PlayerRenewButton from "@/components/player-renew-button";

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

export default function PlayerProfile({player}: Readonly<Props>) {
  return (
    <div className="p-4 md:p-6 space-y-1 rounded-lg shadow-lg bg-zinc-100/90 max-w-screen-sm">
      <div>
        <PlayerInfoContainer label="이름">
          <span>{player.name}</span>
        </PlayerInfoContainer>

        <PlayerInfoContainer label="태그">
          <span>{player.tag}</span>
        </PlayerInfoContainer>

        <PlayerInfoContainer label="클럽 태그">
          {player.clubTag ?
            <span>{player.clubTag}</span>
            : <span>❌</span>
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
      <PlayerRenewButton player={player}/>
    </div>
  );
}