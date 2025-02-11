import {BrawlStarsNews} from "@/model/brawlstars/BrawlStarsNews";
import {ApiError, BASE_URL, CacheOptions, ListResponse} from "@/lib/api/api";

export async function fetchGetBrawlStarsNews(options?: CacheOptions): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/brawlstars/news`);
  url.searchParams.append('language', 'KOREAN');
  if (!options) {
    return await fetch(url);
  }

  return await fetch(url, {
    next: {
      tags: ['brawlstars', 'news', url.searchParams.toString()],
      revalidate: options.revalidate
    }
  });
}

export async function getBrawlStarsNews(): Promise<BrawlStarsNews[]> {
  const response = await fetchGetBrawlStarsNews({revalidate: 5 * 60});

  if (response.ok) {
    const data = await response.json() as ListResponse<BrawlStarsNews>;
    return data.content;
  }

  throw await ApiError.create(response);
}