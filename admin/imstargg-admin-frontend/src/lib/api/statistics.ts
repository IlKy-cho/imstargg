import { API_BASE_URL, ApiError, ValueResponse } from "./api";

export async function fetchGetBattleLatestId() {
  const url = new URL(`${API_BASE_URL}/admin/api/statistics/battle/latest-id`);
  return await fetch(url);
}

export async function getBattleLatestId() {
  const response = await fetchGetBattleLatestId();
  if (response.ok) {
    const data = await response.json() as ValueResponse<number>;
    return data.value;
  }

  throw await ApiError.create(response);
}

export async function fetchGetPlayerLatestId() {
  const url = new URL(`${API_BASE_URL}/admin/api/statistics/player/latest-id`);
  return await fetch(url);
}

export async function getPlayerLatestId() {
  const response = await fetchGetPlayerLatestId();
  if (response.ok) {
    const data = await response.json() as ValueResponse<number>;
    return data.value;
  }

  throw await ApiError.create(response);
}
