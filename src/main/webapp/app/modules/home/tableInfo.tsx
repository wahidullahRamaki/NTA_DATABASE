// import './home.scss';
// import React, { useState, useEffect } from 'react';
// import { DataTable } from 'primereact/datatable';
// import { Column } from 'primereact/column';
// import { useAppDispatch, useAppSelector } from 'app/config/store';
// import { getEntities } from 'app/entities/nta-table/nta-table.reducer';
// import { useNavigate } from 'react-router-dom';

// export default function TableInfo() {
//   const dispatch = useAppDispatch();
//   const navigate = useNavigate();
//   const nTATableList = useAppSelector(state => state.nTATable.entities);

//   useEffect(() => {
//     dispatch(getEntities({}));
//   }, [dispatch]);

//   const products = nTATableList.map(item => ({
//     id: item.id,
//     fullName: item.fullName,
//     fatherName: item.fatherName,
//   }));

//   const onRowSelect = e => {
//     navigate(`/nta-table/${e.data.id}`, { state: { from: '/info-table' } });
//   };

//   return (
//     <div className="card">
//       <DataTable value={products} stripedRows tableStyle={{ minWidth: '50rem' }} onRowClick={onRowSelect}>
//         <Column field="id" header="Number"></Column>
//         <Column field="fullName" header="Full Name"></Column>
//         <Column field="fatherName" header="Father Name"></Column>
//       </DataTable>
//     </div>
//   );
// }
