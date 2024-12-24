import {Player} from "@/model/Player";
import {ListResponse} from "@/model/response/ListResponse";
import {fetchSearchPlayer} from "@/lib/api/api";

export async function searchPlayer(query: string): Promise<Player[]> {
  const response = await fetchSearchPlayer(query);

  if (!response.ok) {
    throw new Error('Failed to fetch player data');
  }

  const data = await response.json() as ListResponse<Player>;
  return data.content;
}
