export const BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL;

export interface CacheOptions {
  revalidate?: number | false;
}

export interface ListResponse<T> {
  content: T[];
}

export interface SliceResponse<T> {
  content: T[];
  hasNext: boolean;
}

export const ApiErrorTypeValue = {
  PLAYER_NOT_FOUND : 'PLAYER_NOT_FOUND',
  PLAYER_RENEW_UNAVAILABLE : 'PLAYER_RENEW_UNAVAILABLE',
  PLAYER_RENEWAL_TOO_MANY: 'PLAYER_RENEWAL_TOO_MANY',
  BRAWLSTARS_IN_MAINTENANCE: 'BRAWLSTARS_IN_MAINTENANCE',
} as const;

export type ApiErrorType = typeof ApiErrorTypeValue[keyof typeof ApiErrorTypeValue];

export interface ErrorResponse {
  type: ApiErrorType;
  message: string;
}

export interface ProblemDetail {
  type: URL;
  title: string | null;
  status: number;
  detail: string | null;
  instance: URL | null;
  properties: Map<string, object> | null;
}

export class ApiError extends Error {
  private constructor(
    public readonly response: Response,
    public readonly error?: ErrorResponse,
    public readonly problemDetail?: ProblemDetail,
    message = `Failed to fetch from ${response.url}. status: ${response.status}`
  ) {
    super(message);
  }

  private static isApiErrorType(type: unknown): type is ApiErrorType {
    return typeof type === 'string' && (type as ApiErrorType) in ApiErrorTypeValue;
  }

  static async create(response: Response): Promise<ApiError> {
    try {
      const json = await response.json();

      if (json && ApiError.isApiErrorType(json.type)) {
        return new ApiError(response, json as ErrorResponse);
      } else {
        const { type, title, status, detail, instance, ...remainingProperties } = json;
        const problemDetail = {
          type: new URL(type),
          title,
          status,
          detail,
          instance: instance ? new URL(instance) : null,
          properties: remainingProperties
        };
        return new ApiError(response, undefined, problemDetail);
      }
    } catch (e) {
      console.log('Failed to parse error response:', e);
      return new ApiError(response);
    }
  }
}