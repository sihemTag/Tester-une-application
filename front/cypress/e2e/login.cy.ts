describe('Login spec', () => {
  beforeEach(() => {
    cy.visit('/login');
  });

  it('Login successfull', () => {
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
  });

  it('Should show validation errors when fields are invalid', () => {
    cy.get('button[type="submit"]').should('be.disabled');

    cy.get('input[formControlName="email"]').type('invalid-email');
    cy.get('input[formControlName="password"]').type('12');

    cy.get('button[type="submit"]').should('be.disabled');
  });
});