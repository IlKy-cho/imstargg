export const DateRangeValue = {
  ONE_DAY: 'ONE_DAY',
  THREE_DAYS: 'THREE_DAYS',
  ONE_WEEK: 'ONE_WEEK',
  TWO_WEEKS: 'TWO_WEEKS',
} as const;

export type DateRange = typeof DateRangeValue[keyof typeof DateRangeValue];

export const DateRangeValues = Object.keys(DateRangeValue) as DateRange[];

export const dateRangeTitle = (dateRange: DateRange): string => {
  switch (dateRange) {
    case DateRangeValue.ONE_DAY:
      return '1일';
    case DateRangeValue.THREE_DAYS:
      return '3일';
    case DateRangeValue.ONE_WEEK:
      return '1주';
    case DateRangeValue.TWO_WEEKS:
      return '2주';
  }
}
