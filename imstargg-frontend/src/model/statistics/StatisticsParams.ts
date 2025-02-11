
import {SoloRankTierRange, SoloRankTierRangeValue} from "../enums/SoloRankTierRange";
import {TrophyRange, TrophyRangeValue} from "../enums/TrophyRange";
import {StatisticsBattleType, StatisticsBattleTypeValue} from "@/model/enums/BattleType";

export interface StatisticsSearchParams {
  type?: StatisticsBattleType;
  trophy?: TrophyRange;
  soloRankTier?: SoloRankTierRange;
}

export class StatisticsParams {

  public readonly type: StatisticsBattleType;
  public readonly trophy: TrophyRange;
  public readonly soloRankTier: SoloRankTierRange;

  constructor(
    type?: StatisticsBattleType,
    trophy?: TrophyRange,
    soloRankTier?: SoloRankTierRange
  ) {
    this.type = type ?? StatisticsBattleTypeValue.ALL;
    this.trophy = trophy ?? TrophyRangeValue.TROPHY_500_PLUS;
    this.soloRankTier = soloRankTier ?? SoloRankTierRangeValue.DIAMOND_PLUS;
  }

  public getSoloRankTierOfType(): SoloRankTierRange | null {
    return this.type === StatisticsBattleTypeValue.SOLO_RANKED ? this.soloRankTier : null;
  }

  public getTrophyOfType(): TrophyRange | null {
    return this.type === StatisticsBattleTypeValue.RANKED ? this.trophy : null;
  }
}

export const searchParamsToStatisticsParams = (searchParams: StatisticsSearchParams): StatisticsParams => {
  return new StatisticsParams(
    searchParams.type,
    searchParams.trophy,
    searchParams.soloRankTier
  );
}