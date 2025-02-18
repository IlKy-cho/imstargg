
import {SoloRankTierRange, SoloRankTierRangeValue} from "../enums/SoloRankTierRange";
import {TrophyRange, TrophyRangeValue} from "../enums/TrophyRange";
import {StatisticsBattleType, StatisticsBattleTypeValue} from "@/model/enums/BattleType";
import { DateRange, DateRangeValue } from "@/model/enums/DateRange";

export interface StatisticsSearchParams {
  type?: StatisticsBattleType;
  dateRange?: DateRange;
  trophy?: TrophyRange;
  soloRankTier?: SoloRankTierRange;
  brawlerId?: number;
}

export class StatisticsParams {

  public readonly type: StatisticsBattleType;
  public readonly dateRange: DateRange;
  public readonly trophy: TrophyRange;
  public readonly soloRankTier: SoloRankTierRange;
  public readonly brawlerId?: number;
  
  constructor(
    type?: StatisticsBattleType,
    dateRange?: DateRange,
    trophy?: TrophyRange,
    soloRankTier?: SoloRankTierRange,
    brawlerId?: number
  ) {
    this.type = type ?? StatisticsBattleTypeValue.ALL;
    this.dateRange = dateRange ?? DateRangeValue.ONE_WEEK;
    this.trophy = trophy ?? TrophyRangeValue.TROPHY_500_PLUS;
    this.soloRankTier = soloRankTier ?? SoloRankTierRangeValue.DIAMOND_PLUS;
    this.brawlerId = brawlerId;
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
    searchParams.dateRange,
    searchParams.trophy,
    searchParams.soloRankTier,
    searchParams.brawlerId
  );
}