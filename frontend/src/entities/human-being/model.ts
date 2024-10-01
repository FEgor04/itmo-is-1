import z from "zod";
import { MoodSchema } from "../enums/mood";
import { WeaponTypeSchema } from "../enums/weapon-type";
import { CarSchema } from "../car/model";

export const BaseHumanBeingSchema = z.object({
  id: z.number().gt(0),
  name: z.string().min(1),
  coordinates: z.object({
    x: z.number().optional(),
    y: z.number(),
  }),
  creationDate: z.coerce.date(),
  realHero: z.boolean().optional(),
  hasToothpick: z.boolean().optional(),
  mood: MoodSchema,
  impactSpeed: z.number().max(108).optional(),
  weaponType: WeaponTypeSchema,
});

export const FetchedHumanBeingSchema = BaseHumanBeingSchema.extend({
  car: CarSchema,
});

export type FetchedHumanBeing = z.infer<typeof FetchedHumanBeingSchema>