import {fetchGetRenewalStatus} from "@/lib/api/api";

export interface RenewalStatusResponse {
  renewing: boolean;
}

export async function getRenewalStatus(tag: string): Promise<RenewalStatusResponse> {
  const response = await fetchGetRenewalStatus(tag);

  if (!response.ok) {
    console.log(`Error status: ${response.status}`);
    console.log(`Error body: ${await response.json()}`);
    throw new Error(`Failed to fetch from ${response.url}.`);
  }

  return await response.json() as RenewalStatusResponse;
}