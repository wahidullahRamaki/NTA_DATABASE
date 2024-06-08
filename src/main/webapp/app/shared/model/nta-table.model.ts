import dayjs from 'dayjs';

export interface INTATable {
  id?: number;
  fullName?: string;
  fatherName?: string;
  jobTitle?: string | null;
  step?: string | null;
  educationDegree?: string | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  salary?: number | null;
  signature?: string | null;
}

export const defaultValue: Readonly<INTATable> = {};
