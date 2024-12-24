import {BrawlStarsNews} from "@/model/brawlstars/BrawlStarsNews";
import {SliceResponse} from "@/model/response/SliceResponse";

export async function getBrawlStarsNewsPage(page: number): Promise<SliceResponse<BrawlStarsNews>> {
  const url = new URL(`${process.env.NEXT_PUBLIC_API_BASE_URL}/api/v1/brawlstars/news`);
  url.searchParams.append('language', 'KOREAN');
  url.searchParams.append('page', page.toString());
  console.log(`Fetch from ${url}`);
  const response = await fetch(url, {
    next: {
        tags: ['brawlstars', 'news']
    }
  });

  if (!response.ok) {
    throw new Error('Failed to fetch from ' + url);
  }

  return await response.json() as SliceResponse<BrawlStarsNews>;
}