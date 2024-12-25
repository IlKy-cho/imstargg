import {Player} from "@/model/Player";
import Image from "next/image";
import {BrawlStarsIconSrc, soloRankTierIconSrc} from "@/components/icon";
import {soloRankTierColor, soloRankTierNumber, SoloRankTierType} from "@/model/enums/SoloRankTier";
import dayjs from "dayjs";
import 'dayjs/locale/ko';
import relativeTime from "dayjs/plugin/relativeTime";

dayjs.locale('ko');
dayjs.extend(relativeTime);

type Props = {
  player: Player;
}

const TrophyIcon = () => (
  <Image
    src={BrawlStarsIconSrc.TROPHY}
    alt="trophy icon"
    width={20}
    height={20}
  />
);

const SoloRankTierIcon = ({tier}: { tier: SoloRankTierType }) => (
  <Image
    src={soloRankTierIconSrc(tier)}
    alt="rank tier icon"
    width={24}
    height={24}
  />
);

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
        <div className="flex items-center gap-2">
          <TrophyIcon/>
          <span className="text-amber-500">{player.trophies.toLocaleString()}</span>
        </div>
      </PlayerInfoContainer>

      <PlayerInfoContainer label="최고 트로피">
        <div className="flex items-center gap-2">
          <TrophyIcon/>
          <span className="text-amber-500">{player.highestTrophies.toLocaleString()}</span>
        </div>
      </PlayerInfoContainer>

      <PlayerInfoContainer label="경쟁전">
        <div className="flex items-center gap-2">
          <SoloRankTierIcon tier={player.soloRankTier}/>
          {player.soloRankTier && (
            <span className={'text-[' + soloRankTierColor(player.soloRankTier) + ']'}>
              {soloRankTierNumber(player.soloRankTier)}
            </span>
          )}
        </div>
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