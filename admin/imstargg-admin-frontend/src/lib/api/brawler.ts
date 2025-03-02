import {Brawler} from "@/model/Brawler";
import {API_BASE_URL, ApiError, ListResponse} from "./api";
import {Gear} from "@/model/Gear";
import {NewBrawlerRequest} from "@/model/request/NewBrawlerRequest";
import {NewBrawlerGearRequest} from "@/model/request/NewBrawlerGearRequest";
import {NewStarPowerRequest} from "@/model/request/NewStarPowerRequest";
import {NewGadgetRequest} from "@/model/request/NewGadgetRequest";
import {GearUpdateRequest} from "@/model/request/GearUpdateRequest";

export async function fetchGetBrawlers(): Promise<Response> {
  const url = new URL(`${API_BASE_URL}/admin/api/brawlers`);
  return await fetch(url);
}

export async function getBrawlers(): Promise<Brawler[]> {
  const response = await fetchGetBrawlers();
  if (!response.ok) {
    throw await ApiError.create(response);
  }
  const data = await response.json() as ListResponse<Brawler>;
  return data.content;
}

export async function fetchGetGears(): Promise<Response> {
  const url = new URL(`${API_BASE_URL}/admin/api/gears`);
  return await fetch(url);
}

export async function getGears(): Promise<Gear[]> {
  const response = await fetchGetGears();
  if (!response.ok) {
    throw await ApiError.create(response);
  }
  const data = await response.json() as ListResponse<Gear>;
  return data.content;
}

export async function fetchRegisterBrawler(request: NewBrawlerRequest) {
  const url = new URL(`${API_BASE_URL}/admin/api/brawlers`);
  return await fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(request)
  });
}

export async function registerBrawler(request: NewBrawlerRequest) {
  const response = await fetchRegisterBrawler(request);
  if (!response.ok) {
    throw await ApiError.create(response);
  }
}

export async function fetchUploadBrawlerProfileImage(brawlerId: number, image: File) {
  const url = new URL(`${API_BASE_URL}/admin/api/brawlers/${brawlerId}/profile-image`);
  const formData = new FormData();
  formData.append('image', image);

  return await fetch(url, {
    method: 'PUT',
    body: formData
  });
}

export async function uploadBrawlerProfileImage(brawlerId: number, image: File) {
  const response = await fetchUploadBrawlerProfileImage(brawlerId, image);
  if (!response.ok) {
    throw await ApiError.create(response);
  }
}

export async function fetchUploadGearImage(brawlStarsId: number, image: File) {
  const url = new URL(`${API_BASE_URL}/admin/api/gears/${brawlStarsId}/image`);
  console.log(image);
  const formData = new FormData();
  formData.append('image', image);

  return await fetch(url, {
    method: 'PUT',
    body: formData
  });
}

export async function uploadGearImage(brawlStarsId: number, image: File) {
  const response = await fetchUploadGearImage(brawlStarsId, image);
  if (!response.ok) {
    throw await ApiError.create(response);
  }
}

export async function fetchUploadGadgetImage(id: number, image: File) {
  const url = new URL(`${API_BASE_URL}/admin/api/gadgets/${id}/image`);
  const formData = new FormData();
  formData.append('image', image);

  return await fetch(url, {
    method: 'PUT',
    body: formData
  });
}

export async function uploadGadgetImage(id: number, image: File) {
  const response = await fetchUploadGadgetImage(id, image);
  if (!response.ok) {
    throw await ApiError.create(response);
  }
}

export async function fetchUploadStarPowerImage(id: number, image: File) {
  const url = new URL(`${API_BASE_URL}/admin/api/starpowers/${id}/image`);
  const formData = new FormData();
  formData.append('image', image);

  return await fetch(url, {
    method: 'PUT',
    body: formData
  });
}

export async function uploadStarPowerImage(id: number, image: File) {
  const response = await fetchUploadStarPowerImage(id, image);
  if (!response.ok) {
    throw await ApiError.create(response);
  }
}

export async function fetchRegisterBrawlerGear(brawlStarsId: number, request: NewBrawlerGearRequest) {
  const url = new URL(`${API_BASE_URL}/admin/api/brawlers/${brawlStarsId}/gears`);
  return await fetch(url, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ brawlStarsId: request.brawlStarsId })
  });
}

export async function registerBrawlerGear(brawlStarsId: number, request: NewBrawlerGearRequest) {
  const response = await fetchRegisterBrawlerGear(brawlStarsId, request);
  if (!response.ok) {
    throw await ApiError.create(response);
  }
}

export async function fetchRegisterStarPower(brawlStarsId: number, request: NewStarPowerRequest) {
  const url = new URL(`${API_BASE_URL}/admin/api/brawlers/${brawlStarsId}/star-powers`);
  return await fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(request)
  });
}

export async function registerStarPower(brawlStarsId: number, request: NewStarPowerRequest) {
  const response = await fetchRegisterStarPower(brawlStarsId, request);
  if (!response.ok) {
    throw await ApiError.create(response);
  }
}

export async function fetchRegisterGadget(brawlStarsId: number, request: NewGadgetRequest) {
  const url = new URL(`${API_BASE_URL}/admin/api/brawlers/${brawlStarsId}/gadgets`);
  return await fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(request)
  });
}

export async function registerGadget(brawlStarsId: number, request: NewGadgetRequest) {
  const response = await fetchRegisterGadget(brawlStarsId, request);
  if (!response.ok) {
    throw await ApiError.create(response);
  }
}

export async function fetchUpdateGear(brawlStarsId: number, request: GearUpdateRequest) {
  const url = new URL(`${API_BASE_URL}/admin/api/gears/${brawlStarsId}`);
  return await fetch(url, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(request),
    credentials: 'include'
  });
}

export async function updateGear(brawlStarsId: number, request: GearUpdateRequest) {
  const response = await fetchUpdateGear(brawlStarsId, request);
  if (!response.ok) {
    throw await ApiError.create(response);
  }
}
