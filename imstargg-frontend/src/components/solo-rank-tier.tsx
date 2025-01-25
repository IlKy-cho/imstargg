import Image from 'next/image';
import {SoloRankTier as SoloRankTierType, SoloRankTierValue} from "@/model/enums/SoloRankTier";
import {cn} from "@/lib/utils";
import BronzeIconSrc from '@/../public/rank/icon_ranked_bronze.png';
import SilverIconSrc from '@/../public/rank/icon_ranked_silver.png';
import GoldIconSrc from '@/../public/rank/icon_ranked_gold.png';
import DiamondIconSrc from '@/../public/rank/icon_ranked_diamond.png';
import MythicIconSrc from '@/../public/rank/icon_ranked_mythic.png';
import LegendaryIconSrc from '@/../public/rank/icon_ranked_legendary.png';
import MastersIconSrc from '@/../public/rank/icon_ranked_masters.png';
import {soloRankTierIconSrc} from "@/lib/solo-rank-tier";

const soloRankTierTextColor = (tier: SoloRankTierType) => {
  switch (tier) {
    case SoloRankTierValue.BRONZE_1:
    case SoloRankTierValue.BRONZE_2:
    case SoloRankTierValue.BRONZE_3:
      return 'text-[#ECA74F]';
    case SoloRankTierValue.SILVER_1:
    case SoloRankTierValue.SILVER_2:
    case SoloRankTierValue.SILVER_3:
      return 'text-[#CBD3F5]';
    case SoloRankTierValue.GOLD_1:
    case SoloRankTierValue.GOLD_2:
    case SoloRankTierValue.GOLD_3:
      return 'text-[#FDF067]';
    case SoloRankTierValue.DIAMOND_1:
    case SoloRankTierValue.DIAMOND_2:
    case SoloRankTierValue.DIAMOND_3:
      return 'text-[#00F3FC]';
    case SoloRankTierValue.MYTHIC_1:
    case SoloRankTierValue.MYTHIC_2:
    case SoloRankTierValue.MYTHIC_3:
      return 'text-[#E050F7]';
    case SoloRankTierValue.LEGENDARY_1:
    case SoloRankTierValue.LEGENDARY_2:
    case SoloRankTierValue.LEGENDARY_3:
      return 'text-[#EB4A59]';
    case SoloRankTierValue.MASTER:
      return 'text-[#FCEF67]';
  }
}

const soloRankTierNumber = (tier: SoloRankTierType) => {
  switch (tier) {
    case SoloRankTierValue.BRONZE_1:
    case SoloRankTierValue.SILVER_1:
    case SoloRankTierValue.GOLD_1:
    case SoloRankTierValue.DIAMOND_1:
    case SoloRankTierValue.MYTHIC_1:
    case SoloRankTierValue.LEGENDARY_1:
      return 'I';
    case SoloRankTierValue.BRONZE_2:
    case SoloRankTierValue.SILVER_2:
    case SoloRankTierValue.GOLD_2:
    case SoloRankTierValue.DIAMOND_2:
    case SoloRankTierValue.MYTHIC_2:
    case SoloRankTierValue.LEGENDARY_2:
      return 'II';
    case SoloRankTierValue.BRONZE_3:
    case SoloRankTierValue.SILVER_3:
    case SoloRankTierValue.GOLD_3:
    case SoloRankTierValue.DIAMOND_3:
    case SoloRankTierValue.MYTHIC_3:
    case SoloRankTierValue.LEGENDARY_3:
      return 'III';
    default:
      return null;
  }
}


export default function SoloRankTier({tier}: Readonly<{ tier: SoloRankTierType }>) {
  return (
    <span className="inline-flex items-center gap-0.5">
      <Image
        src={soloRankTierIconSrc(tier)}
        alt="rank tier icon"
        width={24}
        height={24}
        className="h-full w-auto object-contain"
      />
      <span
        className={cn(soloRankTierTextColor(tier), "text-base")}
        style={{
          textShadow: '-0.5px -0.5px 0 #000, 0.5px -0.5px 0 #000, -0.5px 0.5px 0 #000, 0.5px 0.5px 0 #000'
        }}
      >
        {soloRankTierNumber(tier)}
      </span>
    </span>
  );
}