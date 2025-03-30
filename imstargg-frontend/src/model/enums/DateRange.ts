import {Temporal} from "@js-temporal/polyfill";

export const DateRangeValue = {
  THREE_DAYS: 'THREE_DAYS',
  ONE_WEEK: 'ONE_WEEK',
  TWO_WEEKS: 'TWO_WEEKS',
  ONE_MONTH: 'ONE_MONTH',
} as const;

export type DateRange = typeof DateRangeValue[keyof typeof DateRangeValue];

export const DateRangeValues = Object.keys(DateRangeValue) as DateRange[];

export const dateRangeTitle = (dateRange: DateRange): string => {
  switch (dateRange) {
    case DateRangeValue.THREE_DAYS:
      return '3일';
    case DateRangeValue.ONE_WEEK:
      return '1주';
    case DateRangeValue.TWO_WEEKS:
      return '2주';
    case DateRangeValue.ONE_MONTH:
      return '1개월';
  }
}

export const calculateEndDate = (date: Temporal.PlainDate, dateRange: DateRange): Temporal.PlainDate => {
  switch (dateRange) {
    case DateRangeValue.THREE_DAYS:
      return date.subtract({ days: 3 });
    case DateRangeValue.ONE_WEEK:
      return date.subtract({ weeks: 1 });
    case DateRangeValue.TWO_WEEKS:
      return date.subtract({ weeks: 2 });
    case DateRangeValue.ONE_MONTH:
      return date.subtract({ months: 1 });
  }
}