import {BrawlStarsNews} from "@/model/brawlstars/BrawlStarsNews";
import {SliceResponse} from "@/model/response/SliceResponse";
import {ApiError, fetchGetBrawlStarsNews} from "@/lib/api/api";

export async function getBrawlStarsNewsPage(page: number): Promise<SliceResponse<BrawlStarsNews>> {
  const response = await fetchGetBrawlStarsNews(page, {revalidate: 60 * 60});

  if (response.ok) {
    return await response.json() as SliceResponse<BrawlStarsNews>;
  }

  throw new ApiError(response);
}