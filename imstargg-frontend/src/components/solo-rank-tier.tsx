import Image from 'next/image';
import {SoloRankTier as SoloRankTierType, SoloRankTierValue} from "@/model/enums/SoloRankTier";
import {cn} from "@/lib/utils";
import {soloRankTierIconSrc, soloRankTierNumber} from "@/lib/solo-rank-tier";

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


export default function SoloRankTier({tier}: Readonly<{ tier: SoloRankTierType }>) {
  return (
    <span className="inline-flex items-center gap-0.5">
      <Image
        src={soloRankTierIconSrc(tier)}
        alt="rank tier icon"
        className="w-4 sm:w-5"
      />
      <span
        className={cn(soloRankTierTextColor(tier), "text-xs sm:text-sm")}
        style={{
          textShadow: '-0.5px -0.5px 0 #000, 0.5px -0.5px 0 #000, -0.5px 0.5px 0 #000, 0.5px 0.5px 0 #000'
        }}
      >
        {soloRankTierNumber(tier)}
      </span>
    </span>
  );
}