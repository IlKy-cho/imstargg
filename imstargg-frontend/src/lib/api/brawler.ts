import {Brawler} from "@/model/Brawler";
import {fetchGetBrawlers} from "@/lib/api/api";
import {ListResponse} from "@/model/response/ListResponse";

export async function getBrawlerList() : Promise<Brawler[]> {
  const response = await fetchGetBrawlers({revalidate: 300});

  if (response.ok) {
    const data = await response.json() as ListResponse<Brawler>;
    return data.content;
  }

  console.log(`Failed to fetch from ${response.url}. status: ${response.status}, body: ${response.body}`);
  throw new Error(`Failed to fetch from ${response.url}.`);
}

export async function getBrawlerMap() : Promise<Map<number, Brawler>> {
  const brawlers = await getBrawlerList();
  return new Map(brawlers.map(brawler => [brawler.id, brawler]));
}