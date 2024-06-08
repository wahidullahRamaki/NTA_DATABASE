import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { INTATable } from 'app/shared/model/nta-table.model';
import { getEntity, updateEntity, createEntity, reset } from './nta-table.reducer';

export const NTATableUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const nTATableEntity = useAppSelector(state => state.nTATable.entity);
  const loading = useAppSelector(state => state.nTATable.loading);
  const updating = useAppSelector(state => state.nTATable.updating);
  const updateSuccess = useAppSelector(state => state.nTATable.updateSuccess);

  const handleClose = () => {
    navigate('/nta-table' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.salary !== undefined && typeof values.salary !== 'number') {
      values.salary = Number(values.salary);
    }

    const entity = {
      ...nTATableEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...nTATableEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ntaDatabaseApp.nTATable.home.createOrEditLabel" data-cy="NTATableCreateUpdateHeading">
            <Translate contentKey="ntaDatabaseApp.nTATable.home.createOrEditLabel">Create or edit a NTATable</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="nta-table-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('ntaDatabaseApp.nTATable.fullName')}
                id="nta-table-fullName"
                name="fullName"
                data-cy="fullName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ntaDatabaseApp.nTATable.fatherName')}
                id="nta-table-fatherName"
                name="fatherName"
                data-cy="fatherName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ntaDatabaseApp.nTATable.jobTitle')}
                id="nta-table-jobTitle"
                name="jobTitle"
                data-cy="jobTitle"
                type="text"
              />
              <ValidatedField
                label={translate('ntaDatabaseApp.nTATable.step')}
                id="nta-table-step"
                name="step"
                data-cy="step"
                type="text"
              />
              <ValidatedField
                label={translate('ntaDatabaseApp.nTATable.educationDegree')}
                id="nta-table-educationDegree"
                name="educationDegree"
                data-cy="educationDegree"
                type="text"
              />
              <ValidatedField
                label={translate('ntaDatabaseApp.nTATable.startDate')}
                id="nta-table-startDate"
                name="startDate"
                data-cy="startDate"
                type="date"
              />
              <ValidatedField
                label={translate('ntaDatabaseApp.nTATable.endDate')}
                id="nta-table-endDate"
                name="endDate"
                data-cy="endDate"
                type="date"
              />
              <ValidatedField
                label={translate('ntaDatabaseApp.nTATable.salary')}
                id="nta-table-salary"
                name="salary"
                data-cy="salary"
                type="text"
              />
              <ValidatedField
                label={translate('ntaDatabaseApp.nTATable.signature')}
                id="nta-table-signature"
                name="signature"
                data-cy="signature"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/nta-table" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default NTATableUpdate;
