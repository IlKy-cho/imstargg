import {Brawler} from "@/model/Brawler";
import {ApiError, fetchGetBrawlers} from "@/lib/api/api";
import {ListResponse} from "@/model/response/ListResponse";

export async function getBrawlers() : Promise<Brawler[]> {
  const response = await fetchGetBrawlers({revalidate: 60 * 60});

  if (response.ok) {
    const data = await response.json() as ListResponse<Brawler>;
    return data.content;
  }

  throw new ApiError(response);
}
