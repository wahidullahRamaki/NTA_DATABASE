import './home.scss';

import React, { useEffect } from 'react';
import { Link, Navigate, useNavigate } from 'react-router-dom';
import { Translate, translate } from 'react-jhipster';
import { Row, Col, Alert, Button } from 'reactstrap';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { setLocale } from 'app/shared/reducers/locale';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);
  const currentLocale = useAppSelector(state => state.locale.currentLocale);
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  return (
    <Row className="justify-content-center">
      <Col md="4" style={{ textAlign: 'center', border: 'none', marginTop: '100px' }}>
        <div style={{ fontSize: 30, fontWeight: 'bold' }}>
          {' '}
          <img src="content/images/mcit-logo.png" style={{ marginTop: '-40px' }} width={'360px'} height={'400px'} />
          <br />
          <Translate contentKey="home.title">NTA Employee's Databse</Translate>
          <br />
          <br />
        </div>

        <h2></h2>
        {account?.login ? (
          <Button color="success" onClick={e => navigate('/tableInfo')}>
            <Translate contentKey="home.buttonTitle">You are logged in as user </Translate>&nbsp;{account.login}.
          </Button>
        ) : (
          <Button style={{ fontFamily: translate('settings.font') }} color="success" onClick={e => navigate('/login')}>
            <Translate contentKey="home.button">Click here for the login screen</Translate>
          </Button>
        )}
      </Col>
    </Row>
  );
};

export default Home;
