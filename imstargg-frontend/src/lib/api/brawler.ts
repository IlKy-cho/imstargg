import {Brawler} from "@/model/Brawler";
import {fetchGetBrawlers} from "@/lib/api/api";
import {ListResponse} from "@/model/response/ListResponse";
import {ApiError} from "@/model/response/error";

export async function getBrawlers() : Promise<Brawler[]> {
  const response = await fetchGetBrawlers({revalidate: 60 * 60});

  if (response.ok) {
    const data = await response.json() as ListResponse<Brawler>;
    return data.content;
  }

  throw await ApiError.create(response);
}
