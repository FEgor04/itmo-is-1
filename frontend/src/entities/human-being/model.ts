import { HumanBeingDto } from "@/shared/api.gen";
import { MUST_BE_INT_CONFIG, REQUIRED_CONFIG } from "@/shared/zod";
import z from "zod";
import { BaseCarSchema } from "../car/model";
import { MoodSchema } from "../enums/mood";
import { WeaponTypeSchema } from "../enums/weapon-type";

export const BaseHumanBeingSchema = z.object({
  id: z.number().gt(0),
  name: z.string(REQUIRED_CONFIG).min(1, REQUIRED_CONFIG),
  coordinates: z.object({
    x: z.coerce.number(MUST_BE_INT_CONFIG),
    y: z.coerce.number(MUST_BE_INT_CONFIG),
  }),
  creationDate: z.coerce.date(REQUIRED_CONFIG),
  realHero: z.boolean(),
  hasToothpick: z.boolean(),
  mood: MoodSchema,
  impactSpeed: z.coerce
    .number(MUST_BE_INT_CONFIG)
    .max(108, "Значение должно быть меньше 108")
    .optional(),
  weaponType: WeaponTypeSchema,
  ownerId: z.number(),
});

export const FetchedHumanBeingSchema = BaseHumanBeingSchema.extend({
  car: BaseCarSchema,
});
export const FetchedHumanBeingSchemaKeys = z.enum([
  "id",
  "name",
  "coordinates_x",
  "coordinates_y",
  "creationDate",
  "realHero",
  "hasToothpick",
  "mood",
  "impactSpeed",
  "weaponType",
  "car_model",
  "car_brand",
  "car_cool",
  "car_color",
]);

export function parseHumanBeingDTO(data: HumanBeingDto) {
  return FetchedHumanBeingSchema.parse({
    id: data.id,
    name: data.name,
    coordinates: {
      x: data.x,
      y: data.y,
    },
    car: data.car,
    mood: data.mood,
    impactSpeed: data.impactSpeed,
    weaponType: data.weaponType,
    realHero: data.realHero,
    hasToothpick: data.hasToothpick,
    creationDate: data.creationDate,
    ownerId: data.ownerId,
  });
}

export type FetchedHumanBeing = z.infer<typeof FetchedHumanBeingSchema>;
