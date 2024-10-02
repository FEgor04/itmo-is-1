import { z, ZodType } from "zod";

const ALLOWED_PAGE_SIZE = [10, 25, 50];
const DEFAULT_PAGE_SIZE = ALLOWED_PAGE_SIZE[0];

export const PaginatedQuerySchema = z.object({
  page: z.number().min(1).catch(1),
  pageSize: z
    .number()
    .refine((value) => ALLOWED_PAGE_SIZE.includes(value))
    .catch(DEFAULT_PAGE_SIZE),
});

export const PaginatedResponseSchema = <T>(value: ZodType<T>) =>
  z.object({
    page: z.number().min(1),
    pageSize: z.number().refine((value) => ALLOWED_PAGE_SIZE.includes(value)),
    total: z.number(),
    values: z.array(value),
  });
