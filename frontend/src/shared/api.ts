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
  impactSpeed: number;
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
  impactSpeed: number;
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

import type { AxiosInstance, AxiosRequestConfig, AxiosResponse, HeadersDefaults, ResponseType } from "axios";
import axios from "axios";

export type QueryParamsType = Record<string | number, any>;

export interface FullRequestParams extends Omit<AxiosRequestConfig, "data" | "params" | "url" | "responseType"> {
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

export type RequestParams = Omit<FullRequestParams, "body" | "method" | "query" | "path">;

export interface ApiConfig<SecurityDataType = unknown> extends Omit<AxiosRequestConfig, "data" | "cancelToken"> {
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

  constructor({ securityWorker, secure, format, ...axiosConfig }: ApiConfig<SecurityDataType> = {}) {
    this.instance = axios.create({ ...axiosConfig, baseURL: axiosConfig.baseURL || "http://localhost:8080" });
    this.secure = secure;
    this.format = format;
    this.securityWorker = securityWorker;
  }

  public setSecurityData = (data: SecurityDataType | null) => {
    this.securityData = data;
  };

  protected mergeRequestParams(params1: AxiosRequestConfig, params2?: AxiosRequestConfig): AxiosRequestConfig {
    const method = params1.method || (params2 && params2.method);

    return {
      ...this.instance.defaults,
      ...params1,
      ...(params2 || {}),
      headers: {
        ...((method && this.instance.defaults.headers[method.toLowerCase() as keyof HeadersDefaults]) || {}),
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
      const propertyContent: any[] = property instanceof Array ? property : [property];

      for (const formItem of propertyContent) {
        const isFileType = formItem instanceof Blob || formItem instanceof File;
        formData.append(key, isFileType ? formItem : this.stringifyFormItem(formItem));
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

    if (type === ContentType.FormData && body && body !== null && typeof body === "object") {
      body = this.createFormData(body as Record<string, unknown>);
    }

    if (type === ContentType.Text && body && body !== null && typeof body !== "string") {
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
export class Api<SecurityDataType extends unknown> extends HttpClient<SecurityDataType> {
  humans = {
    /**
     * @description Returns a human by its ID
     *
     * @tags Human Management
     * @name GetHumanById
     * @summary Get a human by ID
     * @request GET:/humans/{id}
     */
    getHumanById: (id: number, params: RequestParams = {}) =>
      this.request<HumanBeingDto, HumanBeingDto>({
        path: `/humans/${id}`,
        method: "GET",
        format: "json",
        ...params,
      }),

    /**
     * @description Updates an existing human's information
     *
     * @tags Human Management
     * @name UpdateHuman
     * @summary Update human information
     * @request PUT:/humans/{id}
     */
    updateHuman: (id: number, data: HumanBeingDto, params: RequestParams = {}) =>
      this.request<HumanBeingDto, HumanBeingDto>({
        path: `/humans/${id}`,
        method: "PUT",
        body: data,
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
     * @request DELETE:/humans/{id}
     */
    deleteHuman: (id: number, params: RequestParams = {}) =>
      this.request<void, void>({
        path: `/humans/${id}`,
        method: "DELETE",
        ...params,
      }),

    /**
     * No description
     *
     * @tags Human Management
     * @name GetAllHumans
     * @request GET:/humans
     */
    getAllHumans: (
      query: {
        /** @format int32 */
        page: number;
        /** @format int32 */
        pageSize: number;
        sortBy: "id" | "name" | "creationDate" | "realHero" | "hasToothpick" | "mood" | "impactSpeed" | "weaponType";
        sortDirection: "asc" | "desc";
        name?: string;
      },
      params: RequestParams = {},
    ) =>
      this.request<PaginatedResponseHumanBeingDto, PaginatedResponseHumanBeingDto>({
        path: `/humans`,
        method: "GET",
        query: query,
        ...params,
      }),

    /**
     * @description Creates a new human and returns its details
     *
     * @tags Human Management
     * @name CreateHuman
     * @summary Create a new human
     * @request POST:/humans
     */
    createHuman: (data: CreateHumanBeingDto, params: RequestParams = {}) =>
      this.request<HumanBeingDto, HumanBeingDto>({
        path: `/humans`,
        method: "POST",
        body: data,
        type: ContentType.Json,
        format: "json",
        ...params,
      }),
  };
  cars = {
    /**
     * @description Returns a car by its ID
     *
     * @tags Car Management
     * @name GetCarById
     * @summary Get a car by ID
     * @request GET:/cars/{id}
     */
    getCarById: (id: number, params: RequestParams = {}) =>
      this.request<CarDTO, CarDTO>({
        path: `/cars/${id}`,
        method: "GET",
        format: "json",
        ...params,
      }),

    /**
     * @description Updates an existing car's information
     *
     * @tags Car Management
     * @name UpdateCar
     * @summary Update car information
     * @request PUT:/cars/{id}
     */
    updateCar: (id: number, data: CarDTO, params: RequestParams = {}) =>
      this.request<CarDTO, CarDTO>({
        path: `/cars/${id}`,
        method: "PUT",
        body: data,
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
     * @request DELETE:/cars/{id}
     */
    deleteCar: (id: number, params: RequestParams = {}) =>
      this.request<void, void>({
        path: `/cars/${id}`,
        method: "DELETE",
        ...params,
      }),

    /**
     * No description
     *
     * @tags Car Management
     * @name GetAllCars
     * @request GET:/cars
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
      },
      params: RequestParams = {},
    ) =>
      this.request<PaginatedResponseCarDTO, PaginatedResponseCarDTO>({
        path: `/cars`,
        method: "GET",
        query: query,
        ...params,
      }),

    /**
     * @description Creates a new car and returns its details
     *
     * @tags Car Management
     * @name CreateCar
     * @summary Create a new car
     * @request POST:/cars
     */
    createCar: (data: CreateCarDTO, params: RequestParams = {}) =>
      this.request<CarDTO, CarDTO>({
        path: `/cars`,
        method: "POST",
        body: data,
        type: ContentType.Json,
        format: "json",
        ...params,
      }),
  };
}
