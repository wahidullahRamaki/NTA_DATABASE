import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './nta-table.reducer';

export const NTATableDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, [dispatch, id]);

  const nTATableEntity = useAppSelector(state => state.nTATable.entity);

  const renderDetailRow = (labelKey, value) => (
    <>
      <Col md="6">
        <dt>
          <Translate contentKey={labelKey} />
        </dt>
        <dd>{value}</dd>
      </Col>
    </>
  );

  return (
    <Row>
      <Col md="8">
        <h2 data-cy="nTATableDetailsHeading">
          <Translate contentKey="ntaDatabaseApp.nTATable.detail.title">NTATable</Translate>
        </h2>
        <dl className="jh-entity-details row">
          {renderDetailRow('global.field.id', nTATableEntity.id)}
          {renderDetailRow('ntaDatabaseApp.nTATable.fullName', nTATableEntity.fullName)}
          {renderDetailRow('ntaDatabaseApp.nTATable.fatherName', nTATableEntity.fatherName)}
          {renderDetailRow('ntaDatabaseApp.nTATable.jobTitle', nTATableEntity.jobTitle)}
          {renderDetailRow('ntaDatabaseApp.nTATable.step', nTATableEntity.step)}
          {renderDetailRow('ntaDatabaseApp.nTATable.educationDegree', nTATableEntity.educationDegree)}
          {renderDetailRow(
            'ntaDatabaseApp.nTATable.startDate',
            nTATableEntity.startDate ? <TextFormat value={nTATableEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null,
          )}
          {renderDetailRow(
            'ntaDatabaseApp.nTATable.endDate',
            nTATableEntity.endDate ? <TextFormat value={nTATableEntity.endDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null,
          )}
          {renderDetailRow('ntaDatabaseApp.nTATable.salary', nTATableEntity.salary)}
          {renderDetailRow('ntaDatabaseApp.nTATable.signature', nTATableEntity.signature)}
        </dl>
        <Button tag={Link} to="/nta-table" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/nta-table/${nTATableEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default NTATableDetail;
