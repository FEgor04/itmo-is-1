/* eslint-disable */
/* tslint:disable */
/*
 * ---------------------------------------------------------------
 * ## THIS FILE WAS GENERATED VIA SWAGGER-TYPESCRIPT-API        ##
 * ##                                                           ##
 * ## AUTHOR: acacode                                           ##
 * ## SOURCE: https://github.com/acacode/swagger-typescript-api ##
 * ---------------------------------------------------------------
 */

export interface PutHumanBeingDto {
  /**
   * The unique identifier of the human being
   * @format int64
   * @example 1
   */
  id?: number | null;
  /**
   * The name of the human being
   * @example "John Doe"
   */
  name: string;
  /**
   * The X coordinate of the human being's location
   * @format double
   * @example 12.34
   */
  x: number;
  /**
   * The Y coordinate of the human being's location
   * @format double
   * @example 56.78
   */
  y: number;
  /**
   * Indicates if the human being is a real hero
   * @example true
   */
  realHero: boolean;
  /**
   * Indicates if the human being has a toothpick
   * @example false
   */
  hasToothpick: boolean;
  /**
   * The ID of the car associated with the human being
   * @format int64
   * @example 100
   */
  carId: number;
  /**
   * The mood of the human being
   * @example "APATHY"
   */
  mood?: "SORROW" | "APATHY" | "CALM" | "FRENZY";
  /**
   * The impact speed of the human being
   * @format int64
   * @example 150
   */
  impactSpeed?: number;
  /**
   * The type of weapon the human being has
   * @example "PISTOL"
   */
  weaponType: "AXE" | "PISTOL" | "MACHINE_GUN";
}

/** DTO for read operations on Car */
export interface CarDTO {
  /**
   * The unique identifier of the car
   * @format int64
   * @example 1
   */
  id: number | null;
  /**
   * The color of the car
   * @example "Red"
   */
  color: string;
  /**
   * The model of the car
   * @example "Civic"
   */
  model: string;
  /**
   * The brand of the car
   * @example "Honda"
   */
  brand: string;
  /**
   * Indicates if the car is cool
   * @example true
   */
  cool: boolean;
  /**
   * The owner of the car
   * @format int64
   * @example 1
   */
  ownerId: number;
}

/** Data Transfer Object for a Human Being */
export interface HumanBeingDto {
  /**
   * The unique identifier of the human being
   * @format int64
   * @example 1
   */
  id?: number | null;
  /**
   * The name of the human being
   * @example "John Doe"
   */
  name: string;
  /**
   * The X coordinate of the human being's location
   * @format double
   * @example 12.34
   */
  x: number;
  /**
   * The Y coordinate of the human being's location
   * @format double
   * @example 56.78
   */
  y: number;
  /**
   * The date of creation of the human being record
   * @format date
   * @example "2023-10-01"
   */
  creationDate: string;
  /**
   * Indicates if the human being is a real hero
   * @example true
   */
  realHero: boolean;
  /**
   * Indicates if the human being has a toothpick
   * @example false
   */
  hasToothpick: boolean;
  /** DTO for read operations on Car */
  car: CarDTO;
  /**
   * The mood of the human being
   * @example "APATHY"
   */
  mood?: "SORROW" | "APATHY" | "CALM" | "FRENZY";
  /**
   * The impact speed of the human being
   * @format int64
   * @example 150
   */
  impactSpeed?: number;
  /**
   * The type of weapon the human being has
   * @example "PISTOL"
   */
  weaponType: "AXE" | "PISTOL" | "MACHINE_GUN";
  /**
   * The owner of the car
   * @format int64
   * @example 1
   */
  ownerId: number;
}

/** DTO for read operations on Car */
export interface UpdateCarDTO {
  /**
   * The color of the car
   * @example "Red"
   */
  color: string;
  /**
   * The model of the car
   * @example "Civic"
   */
  model: string;
  /**
   * The brand of the car
   * @example "Honda"
   */
  brand: string;
  /**
   * Indicates if the car is cool
   * @example true
   */
  cool: boolean;
}

export interface CreateHumanBeingDto {
  /**
   * The name of the human being
   * @example "John Doe"
   */
  name: string;
  /**
   * The X coordinate of the human being's location
   * @format double
   * @example 12.34
   */
  x: number;
  /**
   * The Y coordinate of the human being's location
   * @format double
   * @example 56.78
   */
  y: number;
  /**
   * Indicates if the human being is a real hero
   * @example true
   */
  realHero: boolean;
  /**
   * Indicates if the human being has a toothpick
   * @example false
   */
  hasToothpick: boolean;
  /**
   * The ID of the car associated with the human being
   * @format int64
   * @example 100
   */
  carId: number;
  /**
   * The mood of the human being
   * @example "APATHY"
   */
  mood?: "SORROW" | "APATHY" | "CALM" | "FRENZY";
  /**
   * The impact speed of the human being
   * @format int64
   * @example 150
   */
  impactSpeed?: number;
  /**
   * The type of weapon the human being has
   * @example "PISTOL"
   */
  weaponType: "AXE" | "PISTOL" | "MACHINE_GUN";
}

/** DTO for read operations on Car */
export interface CreateCarDTO {
  /**
   * The color of the car
   * @example "Red"
   */
  color: string;
  /**
   * The model of the car
   * @example "Civic"
   */
  model: string;
  /**
   * The brand of the car
   * @example "Honda"
   */
  brand: string;
  /**
   * Indicates if the car is cool
   * @example true
   */
  cool: boolean;
}

export interface SignUpRequest {
  username: string;
  password: string;
}

/** Error message model */
export interface ErrorMessage {
  /**
   * @format int32
   * @example 404
   */
  statusCode: number;
  /**
   * @format date-time
   * @example "2024-04-11T12:00:00Z"
   */
  timestamp: string;
  /** @example "Resource not found" */
  description: string;
  /** @example "The requested resource could not be found" */
  message: string;
}

/** JWT Response */
export interface JwtResponse {
  /**
   * User ID
   * @format int64
   */
  id: number;
  /** Username */
  username: string;
  /** Access token */
  accessToken: string;
  /** Refresh token */
  refreshToken: string;
}

export interface SignInRequest {
  username: string;
  password: string;
}

/** DTO for read operations on Car */
export interface GetMeResponse {
  /**
   * The username of the user
   * @example "John Doe"
   */
  username: string;
  /**
   * The ID of the user
   * @format int64
   * @example 1
   */
  id: number;
  /**
   * The role of the user
   * @example "ADMIN"
   */
  role: "USER" | "ADMIN";
  /**
   * The status of the admin request
   * @example "PENDING"
   */
  adminRequestStatus: "PENDING" | "APPROVED" | "REJECTED" | "NO_REQUEST";
}

export interface PaginatedResponseHumanBeingDto {
  /** @format int32 */
  page: number;
  /** @format int32 */
  pageSize: number;
  /** @format int32 */
  total: number;
  values: Array<HumanBeingDto>;
}

export interface PaginatedResponseCarDTO {
  /** @format int32 */
  page: number;
  /** @format int32 */
  pageSize: number;
  /** @format int32 */
  total: number;
  values: Array<CarDTO>;
}

export interface AdminRequestDto {
  username: string;
  status: "PENDING" | "APPROVED" | "REJECTED" | "NO_REQUEST";
}

export interface PaginatedResponseAdminRequestDto {
  /** @format int32 */
  page: number;
  /** @format int32 */
  pageSize: number;
  /** @format int32 */
  total: number;
  values: Array<AdminRequestDto>;
}

import type {
  AxiosInstance,
  AxiosRequestConfig,
  AxiosResponse,
  HeadersDefaults,
  ResponseType,
} from "axios";
import axios from "axios";

export type QueryParamsType = Record<string | number, any>;

export interface FullRequestParams
  extends Omit<AxiosRequestConfig, "data" | "params" | "url" | "responseType"> {
  /** set parameter to `true` for call `securityWorker` for this request */
  secure?: boolean;
  /** request path */
  path: string;
  /** content type of request body */
  type?: ContentType;
  /** query params */
  query?: QueryParamsType;
  /** format of response (i.e. response.json() -> format: "json") */
  format?: ResponseType;
  /** request body */
  body?: unknown;
}

export type RequestParams = Omit<
  FullRequestParams,
  "body" | "method" | "query" | "path"
>;

export interface ApiConfig<SecurityDataType = unknown>
  extends Omit<AxiosRequestConfig, "data" | "cancelToken"> {
  securityWorker?: (
    securityData: SecurityDataType | null,
  ) => Promise<AxiosRequestConfig | void> | AxiosRequestConfig | void;
  secure?: boolean;
  format?: ResponseType;
}

export enum ContentType {
  Json = "application/json",
  FormData = "multipart/form-data",
  UrlEncoded = "application/x-www-form-urlencoded",
  Text = "text/plain",
}

export class HttpClient<SecurityDataType = unknown> {
  public instance: AxiosInstance;
  private securityData: SecurityDataType | null = null;
  private securityWorker?: ApiConfig<SecurityDataType>["securityWorker"];
  private secure?: boolean;
  private format?: ResponseType;

  constructor({
    securityWorker,
    secure,
    format,
    ...axiosConfig
  }: ApiConfig<SecurityDataType> = {}) {
    this.instance = axios.create({
      ...axiosConfig,
      baseURL: axiosConfig.baseURL || "http://localhost:8080",
    });
    this.secure = secure;
    this.format = format;
    this.securityWorker = securityWorker;
  }

  public setSecurityData = (data: SecurityDataType | null) => {
    this.securityData = data;
  };

  protected mergeRequestParams(
    params1: AxiosRequestConfig,
    params2?: AxiosRequestConfig,
  ): AxiosRequestConfig {
    const method = params1.method || (params2 && params2.method);

    return {
      ...this.instance.defaults,
      ...params1,
      ...(params2 || {}),
      headers: {
        ...((method &&
          this.instance.defaults.headers[
            method.toLowerCase() as keyof HeadersDefaults
          ]) ||
          {}),
        ...(params1.headers || {}),
        ...((params2 && params2.headers) || {}),
      },
    };
  }

  protected stringifyFormItem(formItem: unknown) {
    if (typeof formItem === "object" && formItem !== null) {
      return JSON.stringify(formItem);
    } else {
      return `${formItem}`;
    }
  }

  protected createFormData(input: Record<string, unknown>): FormData {
    if (input instanceof FormData) {
      return input;
    }
    return Object.keys(input || {}).reduce((formData, key) => {
      const property = input[key];
      const propertyContent: any[] =
        property instanceof Array ? property : [property];

      for (const formItem of propertyContent) {
        const isFileType = formItem instanceof Blob || formItem instanceof File;
        formData.append(
          key,
          isFileType ? formItem : this.stringifyFormItem(formItem),
        );
      }

      return formData;
    }, new FormData());
  }

  public request = async <T = any, _E = any>({
    secure,
    path,
    type,
    query,
    format,
    body,
    ...params
  }: FullRequestParams): Promise<AxiosResponse<T>> => {
    const secureParams =
      ((typeof secure === "boolean" ? secure : this.secure) &&
        this.securityWorker &&
        (await this.securityWorker(this.securityData))) ||
      {};
    const requestParams = this.mergeRequestParams(params, secureParams);
    const responseFormat = format || this.format || undefined;

    if (
      type === ContentType.FormData &&
      body &&
      body !== null &&
      typeof body === "object"
    ) {
      body = this.createFormData(body as Record<string, unknown>);
    }

    if (
      type === ContentType.Text &&
      body &&
      body !== null &&
      typeof body !== "string"
    ) {
      body = JSON.stringify(body);
    }

    return this.instance.request({
      ...requestParams,
      headers: {
        ...(requestParams.headers || {}),
        ...(type ? { "Content-Type": type } : {}),
      },
      params: query,
      responseType: responseFormat,
      data: body,
      url: path,
    });
  };
}

/**
 * @title Information systems course
 * @version 1.0.0
 * @baseUrl http://localhost:8080
 *
 * Sample API of the First lab
 */
export class Api<
  SecurityDataType extends unknown,
> extends HttpClient<SecurityDataType> {
  api = {
    /**
     * @description Returns a human by its ID
     *
     * @tags Human Management
     * @name GetHumanById
     * @summary Get a human by ID
     * @request GET:/api/humans/{id}
     * @secure
     */
    getHumanById: (id: number, params: RequestParams = {}) =>
      this.request<HumanBeingDto, HumanBeingDto>({
        path: `/api/humans/${id}`,
        method: "GET",
        secure: true,
        format: "json",
        ...params,
      }),

    /**
     * @description Updates an existing human's information
     *
     * @tags Human Management
     * @name UpdateHuman
     * @summary Update human information
     * @request PUT:/api/humans/{id}
     * @secure
     */
    updateHuman: (
      id: number,
      data: PutHumanBeingDto,
      params: RequestParams = {},
    ) =>
      this.request<HumanBeingDto, HumanBeingDto>({
        path: `/api/humans/${id}`,
        method: "PUT",
        body: data,
        secure: true,
        type: ContentType.Json,
        format: "json",
        ...params,
      }),

    /**
     * @description Deletes a human by its ID
     *
     * @tags Human Management
     * @name DeleteHuman
     * @summary Delete a human
     * @request DELETE:/api/humans/{id}
     * @secure
     */
    deleteHuman: (id: number, params: RequestParams = {}) =>
      this.request<void, void>({
        path: `/api/humans/${id}`,
        method: "DELETE",
        secure: true,
        ...params,
      }),

    /**
     * @description Returns a car by its ID
     *
     * @tags Car Management
     * @name GetCarById
     * @summary Get a car by ID
     * @request GET:/api/cars/{id}
     * @secure
     */
    getCarById: (id: number, params: RequestParams = {}) =>
      this.request<CarDTO, CarDTO>({
        path: `/api/cars/${id}`,
        method: "GET",
        secure: true,
        format: "json",
        ...params,
      }),

    /**
     * @description Updates an existing car's information
     *
     * @tags Car Management
     * @name UpdateCar
     * @summary Update car information
     * @request PUT:/api/cars/{id}
     * @secure
     */
    updateCar: (id: number, data: UpdateCarDTO, params: RequestParams = {}) =>
      this.request<CarDTO, CarDTO>({
        path: `/api/cars/${id}`,
        method: "PUT",
        body: data,
        secure: true,
        type: ContentType.Json,
        format: "json",
        ...params,
      }),

    /**
     * @description Deletes a car by its ID
     *
     * @tags Car Management
     * @name DeleteCar
     * @summary Delete a car
     * @request DELETE:/api/cars/{id}
     * @secure
     */
    deleteCar: (id: number, params: RequestParams = {}) =>
      this.request<void, void>({
        path: `/api/cars/${id}`,
        method: "DELETE",
        secure: true,
        ...params,
      }),

    /**
     * No description
     *
     * @tags Human Management
     * @name GetAllHumans
     * @request GET:/api/humans
     * @secure
     */
    getAllHumans: (
      query: {
        /** @format int32 */
        page: number;
        /** @format int32 */
        pageSize: number;
        sortBy:
          | "id"
          | "name"
          | "creationDate"
          | "realHero"
          | "hasToothpick"
          | "mood"
          | "impactSpeed"
          | "weaponType"
          | "coordinates.x"
          | "coordinates.y"
          | "carId"
          | "car.brand"
          | "car.model"
          | "car.cool"
          | "car.color";
        sortDirection: "asc" | "desc";
        name?: string;
        /** @format double */
        impactSpeedLT?: number;
      },
      params: RequestParams = {},
    ) =>
      this.request<
        PaginatedResponseHumanBeingDto,
        PaginatedResponseHumanBeingDto
      >({
        path: `/api/humans`,
        method: "GET",
        query: query,
        secure: true,
        ...params,
      }),

    /**
     * @description Creates a new human and returns its details
     *
     * @tags Human Management
     * @name CreateHuman
     * @summary Create a new human
     * @request POST:/api/humans
     * @secure
     */
    createHuman: (data: CreateHumanBeingDto, params: RequestParams = {}) =>
      this.request<HumanBeingDto, HumanBeingDto>({
        path: `/api/humans`,
        method: "POST",
        body: data,
        secure: true,
        type: ContentType.Json,
        format: "json",
        ...params,
      }),

    /**
     * No description
     *
     * @tags Car Management
     * @name GetAllCars
     * @request GET:/api/cars
     * @secure
     */
    getAllCars: (
      query: {
        /** @format int32 */
        page: number;
        /** @format int32 */
        pageSize: number;
        sortBy: "id" | "brand" | "model" | "cool" | "color";
        sortDirection: "asc" | "desc";
        model?: string;
        brand?: string;
        color?: string;
      },
      params: RequestParams = {},
    ) =>
      this.request<PaginatedResponseCarDTO, PaginatedResponseCarDTO>({
        path: `/api/cars`,
        method: "GET",
        query: query,
        secure: true,
        ...params,
      }),

    /**
     * @description Creates a new car and returns its details
     *
     * @tags Car Management
     * @name CreateCar
     * @summary Create a new car
     * @request POST:/api/cars
     * @secure
     */
    createCar: (data: CreateCarDTO, params: RequestParams = {}) =>
      this.request<CarDTO, CarDTO>({
        path: `/api/cars`,
        method: "POST",
        body: data,
        secure: true,
        type: ContentType.Json,
        format: "json",
        ...params,
      }),

    /**
     * @description Registers a new user with provided details and generates JWT token
     *
     * @tags Authorization and Registration
     * @name Register
     * @summary User registration
     * @request POST:/api/auth/register
     */
    register: (data: SignUpRequest, params: RequestParams = {}) =>
      this.request<JwtResponse, ErrorMessage>({
        path: `/api/auth/register`,
        method: "POST",
        body: data,
        type: ContentType.Json,
        format: "json",
        ...params,
      }),

    /**
     * @description Refreshes JWT token based on provided refresh token
     *
     * @tags Authorization and Registration
     * @name Refresh
     * @summary Refresh token
     * @request POST:/api/auth/refresh
     */
    refresh: (data: string, params: RequestParams = {}) =>
      this.request<JwtResponse, ErrorMessage>({
        path: `/api/auth/refresh`,
        method: "POST",
        body: data,
        type: ContentType.Json,
        format: "json",
        ...params,
      }),

    /**
     * @description Authenticates user based on provided credentials and generates JWT token
     *
     * @tags Authorization and Registration
     * @name Login
     * @summary User login
     * @request POST:/api/auth/login
     */
    login: (data: SignInRequest, params: RequestParams = {}) =>
      this.request<JwtResponse, ErrorMessage>({
        path: `/api/auth/login`,
        method: "POST",
        body: data,
        type: ContentType.Json,
        format: "json",
        ...params,
      }),

    /**
     * @description Submit admin request
     *
     * @tags Admin Management
     * @name SubmitAdminRequest
     * @summary Submit admin request
     * @request POST:/api/admin/requests/submit
     * @secure
     */
    submitAdminRequest: (params: RequestParams = {}) =>
      this.request<string, string>({
        path: `/api/admin/requests/submit`,
        method: "POST",
        secure: true,
        ...params,
      }),

    /**
     * @description Rejected admin request
     *
     * @tags Admin Management
     * @name RejectedAdminRequest
     * @summary Rejected admin request
     * @request POST:/api/admin/requests/rejected/{username}
     * @secure
     */
    rejectedAdminRequest: (username: string, params: RequestParams = {}) =>
      this.request<string, string>({
        path: `/api/admin/requests/rejected/${username}`,
        method: "POST",
        secure: true,
        ...params,
      }),

    /**
     * @description Approve admin request
     *
     * @tags Admin Management
     * @name ApproveAdminRequest
     * @summary Approve admin request
     * @request POST:/api/admin/requests/approve/{username}
     * @secure
     */
    approveAdminRequest: (username: string, params: RequestParams = {}) =>
      this.request<string, string>({
        path: `/api/admin/requests/approve/${username}`,
        method: "POST",
        secure: true,
        ...params,
      }),

    /**
     * @description Get current user
     *
     * @tags User Management
     * @name Me
     * @summary Get current user
     * @request GET:/api/me
     * @secure
     */
    me: (params: RequestParams = {}) =>
      this.request<GetMeResponse, GetMeResponse>({
        path: `/api/me`,
        method: "GET",
        secure: true,
        ...params,
      }),

    /**
     * @description Returns all admin requests
     *
     * @tags Admin Management
     * @name GetAllAdminRequests
     * @summary Get all admin requests
     * @request GET:/api/admin/requests
     * @secure
     */
    getAllAdminRequests: (
      query: {
        /** @format int32 */
        page: number;
        /** @format int32 */
        pageSize: number;
        sortBy: "username" | "status";
        sortDirection: "asc" | "desc";
        username?: string;
      },
      params: RequestParams = {},
    ) =>
      this.request<
        PaginatedResponseAdminRequestDto,
        PaginatedResponseAdminRequestDto
      >({
        path: `/api/admin/requests`,
        method: "GET",
        query: query,
        secure: true,
        ...params,
      }),
  };
}
