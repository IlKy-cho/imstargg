import {BrawlStarsNews} from "@/model/brawlstars/BrawlStarsNews";
import {SliceResponse} from "@/model/response/SliceResponse";
import {fetchGetBrawlStarsNews} from "@/lib/api/api";

export async function getBrawlStarsNewsPage(page: number): Promise<SliceResponse<BrawlStarsNews>> {
  const response = await fetchGetBrawlStarsNews(page, {revalidate: 60 * 60});

  if (response.ok) {
    return await response.json() as SliceResponse<BrawlStarsNews>;
  }

  console.log(`Failed to fetch from ${response.url}. status: ${response.status}, body: ${response.body}`);
  throw new Error(`Failed to fetch from ${response.url}.`);
}