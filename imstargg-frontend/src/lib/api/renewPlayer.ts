import {fetchRenewPlayer} from "@/lib/api/api";

export interface RenewalResponse {
  accepted: boolean;
}

export async function renewPlayer(tag: string): Promise<RenewalResponse> {
  const response = await fetchRenewPlayer(tag);

  if (response.status === 202) {
    return {accepted: true};
  } else if (response.status === 403) {
    return {accepted: false};
  }

  console.log(`Failed to renew player(${tag}) status: ${response.status}, body: ${await response.json()}`);
  throw new Error(`Failed to renew player(${tag})`);
}