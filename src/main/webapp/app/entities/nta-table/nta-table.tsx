import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './nta-table.reducer';

export const NTATable = () => {
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

  return (
    <div>
      <h2 id="nta-table-heading" data-cy="NTATableHeading">
        <Translate contentKey="ntaDatabaseApp.nTATable.home.title">NTA Tables</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="ntaDatabaseApp.nTATable.home.refreshListLabel">Refresh List</Translate>
          </Button>
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
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="ntaDatabaseApp.nTATable.id">Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('fullName')}>
                  <Translate contentKey="ntaDatabaseApp.nTATable.fullName">Full Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fullName')} />
                </th>
                <th className="hand" onClick={sort('fatherName')}>
                  <Translate contentKey="ntaDatabaseApp.nTATable.fatherName">Father Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fatherName')} />
                </th>
                <th className="hand" onClick={sort('jobTitle')}>
                  <Translate contentKey="ntaDatabaseApp.nTATable.jobTitle">Job Title</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('jobTitle')} />
                </th>
                <th className="hand" onClick={sort('step')}>
                  <Translate contentKey="ntaDatabaseApp.nTATable.step">Step</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('step')} />
                </th>
                <th className="hand" onClick={sort('educationDegree')}>
                  <Translate contentKey="ntaDatabaseApp.nTATable.educationDegree">Education Degree</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('educationDegree')} />
                </th>
                <th className="hand" onClick={sort('startDate')}>
                  <Translate contentKey="ntaDatabaseApp.nTATable.startDate">Start Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('startDate')} />
                </th>
                <th className="hand" onClick={sort('endDate')}>
                  <Translate contentKey="ntaDatabaseApp.nTATable.endDate">End Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('endDate')} />
                </th>
                <th className="hand" onClick={sort('salary')}>
                  <Translate contentKey="ntaDatabaseApp.nTATable.salary">Salary</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('salary')} />
                </th>
                <th className="hand" onClick={sort('signature')}>
                  <Translate contentKey="ntaDatabaseApp.nTATable.signature">Signature</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('signature')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {nTATableList.map((nTATable, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/nta-table/${nTATable.id}`} color="link" size="sm">
                      {nTATable.id}
                    </Button>
                  </td>
                  <td>{nTATable.fullName}</td>
                  <td>{nTATable.fatherName}</td>
                  <td>{nTATable.jobTitle}</td>
                  <td>{nTATable.step}</td>
                  <td>{nTATable.educationDegree}</td>
                  <td>
                    {nTATable.startDate ? <TextFormat type="date" value={nTATable.startDate} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{nTATable.endDate ? <TextFormat type="date" value={nTATable.endDate} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{nTATable.salary}</td>
                  <td>{nTATable.signature}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/nta-table/${nTATable.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/nta-table/${nTATable.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/nta-table/${nTATable.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
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
