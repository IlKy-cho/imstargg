export const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL;

export class ApiError extends Error {
  constructor(message: string) {
    super(message);
  }

  static async create(response: Response) {
    return new ApiError(`Failed to fetch from ${response.url}.\n- status: ${response.status}\nbody: ${JSON.stringify(await response.json())}`);
  }
}

export interface ListResponse<T> {
  content: T[];
}
