import {BrawlStarsNews} from "@/model/brawlstars/BrawlStarsNews";
import {fetchGetBrawlStarsNews} from "@/lib/api/api";
import {ApiError} from "@/model/response/error";
import {ListResponse} from "@/model/response/ListResponse";

export async function getBrawlStarsNews(): Promise<BrawlStarsNews[]> {
  const response = await fetchGetBrawlStarsNews({revalidate: 60 * 60});

  if (response.ok) {
    const data = await response.json() as ListResponse<BrawlStarsNews>;
    return data.content;
  }

  throw await ApiError.create(response);
}