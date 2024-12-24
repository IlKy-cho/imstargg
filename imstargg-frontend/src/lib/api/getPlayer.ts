import {Player} from "@/model/Player";
import {fetchGetPlayer} from "@/lib/api/api";

export interface PlayerResponse {
  player: Player | null;
}

export async function getPlayer(tag: string): Promise<PlayerResponse> {
  const response = await fetchGetPlayer(tag);
  
  if (response.ok) {
    return {player: await response.json() as Player,};
  } else if (response.status === 404) {
    return {player: null};
  }

  console.log(`Failed to fetch from ${response.url}. status: ${response.status}, body: ${response.body}`);
  throw new Error(`Failed to fetch from ${response.url}.`);
}
