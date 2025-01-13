import {TrophyRange} from "@/model/enums/TrophyRange";
import {SoloRankTierRange} from "@/model/enums/SoloRankTierRange";
import {fetchGetBattleEventBrawlerResultStatistics, fetchGetBattleEventBrawlersResultStatistics, fetchGetBattleEventBrawlerRankStatistics, fetchGetBattleEventBrawlersRankStatistics} from "@/lib/api/api";
import {ListResponse} from "@/model/response/ListResponse";
import {BattleEventBrawlerResultStatistics} from "@/model/BattleEventBrawlerResultStatistics";
import {BattleEventBrawlerRankStatistics} from "@/model/BattleEventBrawlerRankStatistics";
import {BattleEventBrawlersRankStatistics} from "@/model/BattleEventBrawlersRankStatistics";
import {BattleEventBrawlersResultStatistics} from "@/model/BattleEventBrawlersResultStatistics";
import {ApiError} from "@/model/response/error";

export async function getBattleEventBrawlerResultStatistics(
  eventId: number,
  date: Date,
  duplicateBrawler: boolean,
  trophyRange?: TrophyRange,
  soloRankTierRange?: SoloRankTierRange
): Promise<BattleEventBrawlerResultStatistics[]> {
  const response = await fetchGetBattleEventBrawlerResultStatistics(
    eventId, date, duplicateBrawler, trophyRange, soloRankTierRange,
    {revalidate: 60 * 60}
  );

  if (response.ok) {
    const data = await response.json() as ListResponse<BattleEventBrawlerResultStatistics>;
    return data.content;
  }

  throw await ApiError.create(response);
}

export async function getBattleEventBrawlersResultStatistics(
  eventId: number,
  date: Date,
  duplicateBrawler: boolean,
  trophyRange?: TrophyRange,
  soloRankTierRange?: SoloRankTierRange
): Promise<BattleEventBrawlersResultStatistics[]> {
  const response = await fetchGetBattleEventBrawlersResultStatistics(
    eventId, date, duplicateBrawler, trophyRange, soloRankTierRange,
    {revalidate: 60 * 60}
  );

  if (response.ok) {
    const data = await response.json() as ListResponse<BattleEventBrawlersResultStatistics>;
    return data.content;
  }

  throw await ApiError.create(response);
}

export async function getBattleEventBrawlerRankStatistics(
  eventId: number,
  date: Date,
  trophyRange: TrophyRange
): Promise<BattleEventBrawlerRankStatistics[]> {
  const response = await fetchGetBattleEventBrawlerRankStatistics(
    eventId, date, trophyRange,
    {revalidate: 60 * 60}
  );

  if (response.ok) {
    const data = await response.json() as ListResponse<BattleEventBrawlerRankStatistics>;
    return data.content;
  }

  throw await ApiError.create(response);
}

export async function getBattleEventBrawlersRankStatistics(
  eventId: number,
  date: Date,
  trophyRange: TrophyRange
): Promise<BattleEventBrawlersRankStatistics[]> {
  const response = await fetchGetBattleEventBrawlersRankStatistics(
    eventId, date, trophyRange,
    {revalidate: 60 * 60}
  );

  if (response.ok) {
    const data = await response.json() as ListResponse<BattleEventBrawlersRankStatistics>;
    return data.content;
  }

  throw await ApiError.create(response);
}
