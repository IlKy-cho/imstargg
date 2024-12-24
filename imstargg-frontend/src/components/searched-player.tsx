import Image from "next/image";
import { Player } from "@/model/Player";
import { useRouter } from "next/navigation";
import { BrawlStarsIconSrc, soloRankTierIconSrc } from "@/components/icon";
import { SoloRankTier, soloRankTierColor, soloRankTierNumber } from "@/model/enums/SoloRankTier";
import dayjs from "dayjs";
import 'dayjs/locale/ko';
import relativeTime from 'dayjs/plugin/relativeTime';

dayjs.locale('ko');
dayjs.extend(relativeTime);


interface SearchedPlayerProps {
  player: Player;
}

export default function SearchedPlayer({ player }: SearchedPlayerProps) {
  const router = useRouter();
  console.log(`text-[${soloRankTierColor(SoloRankTier.BRONZE_1)}]`);

  return (
    <div
      className="p-4 border rounded-lg cursor-pointer hover:bg-zinc-100"
      onClick={() => router.push(`/players/${encodeURIComponent(player.tag)}`)}
    >
      <div className="flex items-center gap-2">
        <span style={{ color: player.nameColor }}>{player.name}</span>
        <span className="text-gray-500">{player.tag}</span>
      </div>
      <div className="text-sm text-gray-600 flex items-center justify-between">
        <div className="flex items-center gap-1">
          <Image
            src={BrawlStarsIconSrc.TROPHY}
            alt="trophy logo"
            width={16}
            height={16}
          />
          {player.trophies}
          {player.soloRankTier ?
            <Image
              src={soloRankTierIconSrc(player.soloRankTier)}
              alt="trophy logo"
              width={16}
              height={16}
            /> : null}
          {player.soloRankTier ?
            <span className={`font-bold text-[${soloRankTierColor(player.soloRankTier)}]`}>
              {soloRankTierNumber(player.soloRankTier)}
            </span>
            : null}
        </div>
        <div>
          최근 업데이트: {dayjs(player.updatedAt).fromNow()}
        </div>
      </div>
    </div>
  );
}