import {TrophyRange} from "@/model/enums/TrophyRange";
import {SoloRankTierRange} from "@/model/enums/SoloRankTierRange";

export interface BattleEventStatisticsParam {
  duplicateBrawler?: boolean;
  trophyRange?: TrophyRange;
  soloRankTierRange?: SoloRankTierRange;
}