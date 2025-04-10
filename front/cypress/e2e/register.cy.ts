describe('Register Form', () => {
    beforeEach(() => {
      cy.visit('/register');
    });
  
    it('should show validation errors when fields are invalid', () => {
      cy.get('button[type="submit"]').should('be.disabled');
  
      cy.get('input[formControlName="firstName"]').type('Te');
      cy.get('input[formControlName="lastName"]').type('St');
      cy.get('input[formControlName="email"]').type('invalid-email');
      cy.get('input[formControlName="password"]').type('12');
  
      cy.get('button[type="submit"]').should('be.disabled');
    });
  
    it('should allow user to register with valid details', () => {
      cy.intercept('POST', '/api/auth/register', {
        statusCode: 201,
      });
  
      cy.get('input[formControlName="firstName"]').type('Test');
      cy.get('input[formControlName="lastName"]').type('User');
      cy.get('input[formControlName="email"]').type('yoga@studio.com');
      cy.get('input[formControlName="password"]').type('test!123');
  
      cy.get('button[type="submit"]').click();
  
      cy.url().should('include', '/login');
    });
  
    it('should display an error message on failed registration', () => {
      cy.intercept('POST', '/api/auth/register', {
        statusCode: 400,
      });
  
      cy.get('input[formControlName="firstName"]').type('Test');
      cy.get('input[formControlName="lastName"]').type('User');
      cy.get('input[formControlName="email"]').type('yoga@studio.com');
      cy.get('input[formControlName="password"]').type('test!123');
  
      cy.get('button[type="submit"]').click();
  
      cy.get('.error').should('contain', 'An error occurred');
    });
  });
  