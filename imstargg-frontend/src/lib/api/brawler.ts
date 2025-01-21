import {Brawler} from "@/model/Brawler";
import {fetchGetBrawler, fetchGetBrawlers} from "@/lib/api/api";
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

export async function getBrawler(id: number) : Promise<Brawler | null> {
  const response = await fetchGetBrawler(id, {revalidate: 60 * 60});

  if (response.ok) {
    return await response.json() as Brawler;
  } else if (response.status === 404) {
    return null;
  }

  throw await ApiError.create(response);
}
