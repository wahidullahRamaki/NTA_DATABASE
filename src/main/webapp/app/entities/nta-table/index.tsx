import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import NTATable from './nta-table';
import NTATableDetail from './nta-table-detail';
import NTATableUpdate from './nta-table-update';
import NTATableDeleteDialog from './nta-table-delete-dialog';

const NTATableRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<NTATable />} />
    <Route path="new" element={<NTATableUpdate />} />
    <Route path=":id">
      <Route index element={<NTATableDetail />} />
      <Route path="edit" element={<NTATableUpdate />} />
      <Route path="delete" element={<NTATableDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default NTATableRoutes;
