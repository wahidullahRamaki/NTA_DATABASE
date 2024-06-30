import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown, faPrint } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import vazirFontBase64 from 'app/fonts/vazirFontBase64'; // Import the font
import jsPDF from 'jspdf';
import 'jspdf-autotable'; // Ensure this import is present

import { getEntities } from './nta-table.reducer';

const NTATable = () => {
  const dispatch = useAppDispatch();
  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const nTATableList = useAppSelector(state => state.nTATable.entities);
  const loading = useAppSelector(state => state.nTATable.loading);
  const totalItems = useAppSelector(state => state.nTATable.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  const handleRowClick = id => {
    navigate(`/nta-table/${id}`);
  };

  const printTable = () => {
    const doc = new jsPDF({
      orientation: 'landscape',
      unit: 'mm',
      format: 'a4',
    });

    // Embed the custom font
    doc.addFileToVFS('Vazir.ttf', vazirFontBase64);
    doc.addFont('Vazir.ttf', 'Vazir', 'normal');
    doc.setFont('Vazir');

    // Add small images on either side of the header
    const leftImage = 'content/images/mcit-logo.png'; // Replace with the correct path if necessary
    const rightImage = 'content/images/download.png'; // Replace with the correct path if necessary

    const imageWidth = 30; // Width of the images
    const imageHeight = 30; // Height of the images
    const imageY = 5; // Y position of the images

    doc.addImage(leftImage, 'PNG', 30, imageY, imageWidth, imageHeight);
    doc.addImage(rightImage, 'PNG', doc.internal.pageSize.width - imageWidth - 30, imageY, imageWidth, imageHeight);

    // Add custom Persian header text
    const headerTexts = ['وزارت مخابرات و تکنالوژی معلوماتی', 'ریاست عمومی حکومتداری دیجیتلی', 'آمریت تنظیم برنامه ها'];
    const fontSize = 12; // Adjust font size as needed

    // Calculate position for centering each text line horizontally
    const pageWidth = doc.internal.pageSize.width;

    headerTexts.forEach((text, index) => {
      const textWidth = (doc.getStringUnitWidth(text) * fontSize) / doc.internal.scaleFactor;
      const textX = (pageWidth - textWidth) / 2;
      const textY = 20 + index * 7; // Adjust vertical positions as necessary
      doc.setFontSize(fontSize);
      doc.text(text, textX, textY);
    });

    // Define columns
    const columns = [
      { header: translate('ntaDatabaseApp.nTATable.signature'), dataKey: 'signature' },
      { header: translate('ntaDatabaseApp.nTATable.salary'), dataKey: 'salary' },
      { header: translate('ntaDatabaseApp.nTATable.endDate'), dataKey: 'endDate' },
      { header: translate('ntaDatabaseApp.nTATable.startDate'), dataKey: 'startDate' },
      { header: translate('ntaDatabaseApp.nTATable.educationDegree'), dataKey: 'educationDegree' },
      { header: translate('ntaDatabaseApp.nTATable.step'), dataKey: 'step' },
      { header: translate('ntaDatabaseApp.nTATable.jobTitle'), dataKey: 'jobTitle' },
      { header: translate('ntaDatabaseApp.nTATable.fatherName'), dataKey: 'fatherName' },
      { header: translate('ntaDatabaseApp.nTATable.fullName'), dataKey: 'fullName' },
      { header: 'Number', dataKey: 'number' },
    ];

    // Define rows
    const rows = nTATableList.map((entity, index) => ({
      number: index + 1,
      fullName: entity.fullName,
      fatherName: entity.fatherName,
      jobTitle: entity.jobTitle,
      step: entity.step,
      educationDegree: entity.educationDegree,
      startDate: entity.startDate ? new Date(entity.startDate).toLocaleDateString('fa-IR') : '',
      endDate: entity.endDate ? new Date(entity.endDate).toLocaleDateString('fa-IR') : '',
      salary: entity.salary,
      signature: entity.signature,
    }));

    // Add table
    doc.autoTable({
      columns,
      body: rows,
      startY: 40, // Adjust based on the header height
      margin: { top: 40 },
      styles: {
        font: 'Vazir',
        fontSize: 10,
        cellPadding: 3,
        halign: 'right', // Align text to the right for Persian
      },
      headStyles: {
        font: 'Vazir',
        fontSize: 10,
        cellPadding: 3,
        halign: 'right',
        fillColor: [41, 128, 185],
        textColor: [255, 255, 255],
        fontStyle: 'bold',
      },
    });

    // Add static footer
    doc.text('قرار شرح فوق جدول هذا ترتیب و تقدیم است', 180, doc.internal.pageSize.height - 10); // Adjust as necessary

    // Save the PDF
    doc.save('nta-table.pdf');
  };

  return (
    <div>
      <Button className="btnv" onClick={printTable}>
        <FontAwesomeIcon icon={faPrint} />
        <Translate contentKey="ntaDatabaseApp.nTATable.home.print">Print</Translate>
      </Button>
      <h2 id="nta-table-heading" data-cy="NTATableHeading">
        <Translate contentKey="ntaDatabaseApp.nTATable.home.title">NTA Tables</Translate>
        <div className="d-flex justify-content-end">
          {/* <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="ntaDatabaseApp.nTATable.home.refreshListLabel">Refresh List</Translate>
          </Button> */}
          <Link to="/nta-table/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="ntaDatabaseApp.nTATable.home.createLabel">Create new NTA Table</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {nTATableList && nTATableList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="ntaDatabaseApp.nTATable.id">Number</Translate> <FontAwesomeIcon icon="sort" onClick={sort('id')} />
                </th>
                <th>
                  <Translate contentKey="ntaDatabaseApp.nTATable.fullName">Full Name</Translate>{' '}
                  <FontAwesomeIcon icon="sort" onClick={sort('fullName')} />
                </th>
                <th>
                  <Translate contentKey="ntaDatabaseApp.nTATable.fatherName">Father Name</Translate>{' '}
                  <FontAwesomeIcon icon="sort" onClick={sort('fatherName')} />
                </th>
                <th>
                  <Translate contentKey="ntaDatabaseApp.nTATable.jobTitle">Job Title</Translate>{' '}
                  <FontAwesomeIcon icon="sort" onClick={sort('jobTitle')} />
                </th>
                <th>
                  <Translate contentKey="ntaDatabaseApp.nTATable.step">Step</Translate>{' '}
                  <FontAwesomeIcon icon="sort" onClick={sort('step')} />
                </th>
                <th>
                  <Translate contentKey="ntaDatabaseApp.nTATable.educationDegree">Education Degree</Translate>{' '}
                  <FontAwesomeIcon icon="sort" onClick={sort('educationDegree')} />
                </th>
                <th>
                  <Translate contentKey="ntaDatabaseApp.nTATable.startDate">Start Date</Translate>{' '}
                  <FontAwesomeIcon icon="sort" onClick={sort('startDate')} />
                </th>
                <th>
                  <Translate contentKey="ntaDatabaseApp.nTATable.endDate">End Date</Translate>{' '}
                  <FontAwesomeIcon icon="sort" onClick={sort('endDate')} />
                </th>
                <th>
                  <Translate contentKey="ntaDatabaseApp.nTATable.salary">Salary</Translate>{' '}
                  <FontAwesomeIcon icon="sort" onClick={sort('salary')} />
                </th>
                <th>
                  <Translate contentKey="ntaDatabaseApp.nTATable.signature">Signature</Translate>{' '}
                  <FontAwesomeIcon icon="sort" onClick={sort('signature')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {nTATableList.map((entity, i) => (
                <tr key={`entity-${i}`} style={{ cursor: 'pointer' }}>
                  <td>{i + 1}</td>
                  <td onClick={() => handleRowClick(entity.id)}>{entity.fullName} </td>
                  <td onClick={() => handleRowClick(entity.id)}>{entity.fatherName}</td>
                  <td onClick={() => handleRowClick(entity.id)}>{entity.jobTitle}</td>
                  <td onClick={() => handleRowClick(entity.id)}>{entity.step}</td>
                  <td onClick={() => handleRowClick(entity.id)}>{entity.educationDegree}</td>
                  <td onClick={() => handleRowClick(entity.id)}>
                    {entity.startDate ? <TextFormat type="date" value={entity.startDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td onClick={() => handleRowClick(entity.id)}>
                    {entity.endDate ? <TextFormat type="date" value={entity.endDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td onClick={() => handleRowClick(entity.id)}>{entity.salary}</td>
                  <td onClick={() => handleRowClick(entity.id)}>{entity.signature}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      {/* <Button tag={Link} to={`/nta-table/${entity.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/nta-table/${entity.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button> */}
                      <Button tag={Link} to={`/nta-table/${entity.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="ntaDatabaseApp.nTATable.home.notFound">No NTA Tables found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={nTATableList && nTATableList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default NTATable;
