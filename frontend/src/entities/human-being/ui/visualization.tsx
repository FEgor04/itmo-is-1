import {
  ChartConfig,
  ChartContainer,
  ChartTooltip,
  ChartTooltipContent,
} from "@/shared/ui/chart";
import { FetchedHumanBeing } from "../model";
import { CartesianGrid, Scatter, ScatterChart, XAxis, YAxis } from "recharts";

type Props = {
  humans: Array<FetchedHumanBeing>;
};

function distinct(values: Array<number>) {
  return Array.from(new Set(values));
}

function useHumansChartConfig(humans: Array<FetchedHumanBeing>): ChartConfig {
  const ownerIds = distinct(humans.map((it) => it.ownerId));
  return ownerIds.reduce(
    (acc, it) => ({
      ...acc,
      [it]: {
        label: it,
        color: "#00ff00",
      },
    }),
    {},
  );
}

export function HumansVisualization({ humans }: Props) {
  const humansFlattened = humans.map((it) => ({
    ...it,
    x: it.coordinates.x,
    y: it.coordinates.y,
  }));
  const owners = distinct(humans.map((it) => it.ownerId));
  const config = useHumansChartConfig(humans);
  console.log(config);
  console.log(humansFlattened);
  return (
    <ChartContainer config={config} className="min-h-[400px] w-full">
      <ScatterChart>
        <CartesianGrid />
        <XAxis dataKey="x" />
        <YAxis dataKey="y" />
        <ChartTooltip content={<ChartTooltipContent />} />
        {owners.map((ownerId) => (
          <Scatter
            fill="red"
            key={ownerId}
            name={String(ownerId)}
            data={humansFlattened.filter((it) => it.ownerId == ownerId)}
          />
        ))}
      </ScatterChart>
    </ChartContainer>
  );
}
