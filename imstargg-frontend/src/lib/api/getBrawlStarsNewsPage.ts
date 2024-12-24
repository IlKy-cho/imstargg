import {BrawlStarsNews} from "@/model/brawlstars/BrawlStarsNews";
import {Slice} from "@/model/response/Slice";

export async function getBrawlStarsNewsPage(page: number): Promise<Slice<BrawlStarsNews>> {
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

  return await response.json() as Slice<BrawlStarsNews>;
}